package com.viveret.pilexa.pi.inputmethods;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1beta1.*;
import com.google.protobuf.ByteString;
import com.viveret.pilexa.pi.AbstractPiLexaServiceProxy;
import com.viveret.pilexa.pi.InputSource;
import com.viveret.pilexa.pi.PiLexaService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.auth.ClientAuthInterceptor;
import io.grpc.stub.StreamObserver;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.SimpleLayout;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.apache.log4j.ConsoleAppender.SYSTEM_OUT;

/**
 * Created by viveret on 2/4/17.
 */
public class GoogleSpeechPiLexaProxy extends AbstractPiLexaServiceProxy implements InputSource {
    static final int BYTES_PER_SAMPLE = 2; // bytes per sample for LINEAR16
    private static final List<String> OAUTH2_SCOPES =
            Arrays.asList("https://www.googleapis.com/auth/cloud-platform");
    private final int samplingRate = 16000;
    final int bytesPerBuffer = samplingRate * BYTES_PER_SAMPLE / 10; // 100 ms
    // Used for testing
    protected TargetDataLine mockDataLine = null;
    private PiLexaService inst;
    private Thread myThread;
    private ManagedChannel channel;
    private SpeechGrpc.SpeechStub speechClient;
    private String myInputString;

    static ManagedChannel createChannel(String host, int port) throws IOException {
        GoogleCredentials creds = GoogleCredentials.getApplicationDefault();
        creds = creds.createScoped(OAUTH2_SCOPES);
        ManagedChannel channel =
                ManagedChannelBuilder.forAddress(host, port)
                        .intercept(new ClientAuthInterceptor(creds, Executors.newSingleThreadExecutor()))
                        .build();

        return channel;
    }

    @Override
    public void start(PiLexaService piLexaService) {
        connect();
        inst = piLexaService;
        String host = "speech.googleapis.com";
        Integer port = 443;
        try {
            channel = createChannel(host, port);
            speechClient = SpeechGrpc.newStub(channel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ConsoleAppender appender = new ConsoleAppender(new SimpleLayout(), SYSTEM_OUT);
        inst.getLog().addAppender(appender);

        myThread = new Thread(this);
        myThread.start();
    }

    @Override
    public void stop() {
        try {
            myThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shutdown() {
        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getInput() {
        myInputString = null;

        StreamObserver<StreamingRecognizeResponse> responseObserver =
                new StreamObserver<StreamingRecognizeResponse>() {
                    @Override
                    public void onNext(StreamingRecognizeResponse response) {
                        List<StreamingRecognitionResult> results = response.getResultsList();
                        if (results.size() < 1) {
                            return;
                        }

                        StreamingRecognitionResult result = results.get(0);
                        String transcript = result.getAlternatives(0).getTranscript();

                        if (result.getIsFinal()) {
                            myInputString = transcript;
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        inst.getLog().log(Level.ERROR, "recognize failed: {0}", error);
                    }

                    @Override
                    public void onCompleted() {
                        inst.getLog().info("recognize completed.");
                    }
                };

        StreamObserver<StreamingRecognizeRequest> requestObserver =
                speechClient.streamingRecognize(responseObserver);
        try {
            // Build and send a StreamingRecognizeRequest containing the parameters for
            // processing the audio.
            RecognitionConfig config =
                    RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                            .setSampleRate(samplingRate)
                            .build();
            StreamingRecognitionConfig streamingConfig =
                    StreamingRecognitionConfig.newBuilder()
                            .setConfig(config)
                            .setInterimResults(false)
                            .setSingleUtterance(false)
                            .build();

            StreamingRecognizeRequest initial =
                    StreamingRecognizeRequest.newBuilder().setStreamingConfig(streamingConfig).build();
            requestObserver.onNext(initial);

            // Get a Line to the audio input device.
            TargetDataLine in = getAudioInputLine();
            byte[] buffer = new byte[bytesPerBuffer];
            int bytesRead;

            in.start();
            // Read and send sequential buffers of audio as additional RecognizeRequests.
            // while (isConnected()
            while (myInputString == null && isConnected()) {
                if ((bytesRead = in.read(buffer, 0, buffer.length)) != -1) {
                    StreamingRecognizeRequest request =
                            StreamingRecognizeRequest.newBuilder()
                                    .setAudioContent(ByteString.copyFrom(buffer, 0, bytesRead))
                                    .build();
                    requestObserver.onNext(request);
                }
            }
        } catch (RuntimeException e) {
            // Cancel RPC.
            requestObserver.onError(e);
            throw e;
        }
        // Mark the end of requests.
        requestObserver.onCompleted();

        return myInputString;
    }

    /**
     * Return a Line to the audio input device.
     */
    private TargetDataLine getAudioInputLine() {
        // For testing
        if (null != mockDataLine) {
            return mockDataLine;
        }

        AudioFormat format = new AudioFormat(samplingRate, BYTES_PER_SAMPLE * 8, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            throw new RuntimeException(String.format(
                    "Device doesn't support LINEAR16 mono raw audio format at {}Hz", samplingRate));
        }
        try {
            TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            // Make sure the line buffer doesn't overflow while we're filling this thread's buffer.
            line.open(format, bytesPerBuffer * 5);
            return line;
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        do {
            inst.interpret(getInput());
        } while (isConnected());
    }
}

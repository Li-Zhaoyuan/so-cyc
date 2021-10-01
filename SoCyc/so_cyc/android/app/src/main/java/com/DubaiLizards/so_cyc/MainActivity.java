package com.DubaiLizards.so_cyc;

// Stuff required for channel
import io.flutter.embedding.android.FlutterActivity;
import androidx.annotation.NonNull;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

// Right now, only this class can link to a MethodChannel
public class MainActivity extends FlutterActivity {
    // Can choose any name for the channel, just make sure same name in the Java code and Dart code
    private static final String CHANNEL = "socyc/test"; // Channel name for testing

    // We will use this main activity as a binder for our java functions
    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler((call, result) -> {
                            if (call.method.equals("GetTestText")) {
                                String myMessage = GetTestText();
                                result.success(myMessage);
                            }
                            // else if (call.method.equals("SOME OTHER FUNCTION"))
                        }
                );
    }

    // Internal test function for linking
    private String GetTestText(){
        return "String From Java Code";
    }

}

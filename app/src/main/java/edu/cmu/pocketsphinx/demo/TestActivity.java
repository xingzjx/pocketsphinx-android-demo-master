package edu.cmu.pocketsphinx.demo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

public class TestActivity extends Activity implements
        RecognitionListener {
    private SpeechRecognizer recognizer;
    private String TAG = "TestActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        new SetupTask(TestActivity.this).execute();
        findViewById(R.id.btn_start_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // recognizer.startListening("digits");
                recognizer.stop();
                recognizer.startListening("menu");

            }
        });

        findViewById(R.id.btn_start_digists).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // recognizer.startListening("digits");
                recognizer.stop();
                recognizer.startListening("digits");
            }
        });

        findViewById(R.id.btn_start_hello).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // recognizer.startListening("digits");
                recognizer.stop();
                recognizer.startListening("hello");
            }
        });

        findViewById(R.id.btn_start_corpus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // recognizer.startListening("digits");
                recognizer.stop();
                recognizer.startListening("corpus");
            }
        });

        findViewById(R.id.btn_start_weather).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // recognizer.startListening("digits");
                recognizer.stop();
                recognizer.startListening("weather");
            }
        });

        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recognizer.stop();
            }
        });

    }


    private static class SetupTask extends AsyncTask<Void, Void, Exception> {
        WeakReference<TestActivity> activityReference;
        SetupTask(TestActivity activity) {
            this.activityReference = new WeakReference<>(activity);
        }
        @Override
        protected Exception doInBackground(Void... params) {
            try {
                Assets assets = new Assets(activityReference.get());
                File assetDir = assets.syncAssets();
                activityReference.get().setupRecognizer(assetDir);
            } catch (IOException e) {
                return e;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Exception result) {
            if (result != null) {
                ((TextView) activityReference.get().findViewById(R.id.caption_text))
                        .setText("Failed to init recognizer " + result);
            } else {
                // activityReference.get().switchSearch(KWS_SEARCH);
                // activityReference.get().recognizer.startListening("digits");
            }
        }
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them

        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))

                .setRawLogDir(assetsDir) // To disable logging of raw audio comment out this call (takes a lot of space on the device)

                .getRecognizer();
        recognizer.addListener(this);

        // Create grammar-based search for digit recognition
        File dmenuGrammar = new File(assetsDir, "menu.gram");
        //recognizer.addKeyphraseSearch("wakeup",  "oh mighty computer");
        recognizer.addGrammarSearch("menu", dmenuGrammar);

        // Create grammar-based search for digit recognition
        File digitsGrammar = new File(assetsDir, "digits.gram");
        //recognizer.addKeyphraseSearch("wakeup",  "oh mighty computer");
        recognizer.addGrammarSearch("digits", digitsGrammar);

        // Create grammar-based search for digit recognition
        File helloGrammar = new File(assetsDir, "hello.gram");
        //recognizer.addKeyphraseSearch("wakeup",  "oh mighty computer");
        recognizer.addGrammarSearch("hello", helloGrammar);

//        File corpusGramer = new File(assetsDir, "corpus.lm");
//        recognizer.addNgramSearch("corpus", corpusGramer);
//
//        // Create language model search
//        File languageModel = new File(assetsDir, "weather.dmp");
//        recognizer.addNgramSearch("weather", languageModel);

    }

    @Override
    public void onBeginningOfSpeech() {
        Log.e(TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onEndOfSpeech() {
        Log.e(TAG, "onEndOfSpeech");
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        Log.e(TAG, "onPartialResult");
        if(hypothesis!=null) {
            Log.e(TAG, "onPartialResult:hypothesis" + "  "+ hypothesis.getHypstr());
        }
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        Log.e(TAG, "onResult");
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(Exception e) {
        Log.e(TAG, e + "");
    }

    @Override
    public void onTimeout() {

    }
}

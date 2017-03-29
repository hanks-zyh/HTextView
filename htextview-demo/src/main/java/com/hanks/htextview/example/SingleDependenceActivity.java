package com.hanks.htextview.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import xyz.hanks.scaletext.ScaleTextView;

public class SingleDependenceActivity extends Activity {

    private FrameLayout frameLayout;
    private String[] texts = {
            "Any android library needs to have an AndroidManifest.xml file",
            "a name or an icon is not AndroidManifest",
            "Contribute to android-library development by creating",
            "出现的出现的Create New Module 窗口"
    };
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_dependence);

        frameLayout = (FrameLayout) findViewById(R.id.container);
        final ScaleTextView view = new ScaleTextView(this);
        view.setText("Using a Library Project means that my overall project will have two manifests");
        frameLayout.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index++;
                if (index >= texts.length) {
                    index = 0;
                }
                view.animateText(texts[index]);
            }
        });
    }
}

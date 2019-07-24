package com.AndroidSingh.NavjyotSingh;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity {

    private ArFragment arFragment;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //used to initialize the object
        init();
    }

    //initialising the objects
    public void init() {
        context = MainActivity.this;
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        //java 8 equivalent to arFragment.setOnTapArPlaneListner(new BaseFragment.OnTapArPlaneListener(){
        //@Override
        //        public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent){
        //    }
        //});

        setArModel();

    }

    private void setArModel() {
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {

            //used to describe orientiation and fixed location in real world
            Anchor anchor = hitResult.createAnchor();

            //buildig a 3D model
            ModelRenderable.builder()
                    .setSource(context, Uri.parse("Lion.sfb"))
                    .build()
                    .thenAccept(modelRenderable -> addModelToScent(anchor, modelRenderable))
                    .exceptionally(throwable -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(throwable.getMessage()).show();
                        return null;
                    });
        });
    }

    private void addModelToScent(Anchor anchor, ModelRenderable modelRenderable) {
        //Anchornode is a node that automatically position in best mode on anchor
        AnchorNode anchorNode = new AnchorNode(anchor);//anchor node is cannot be zoomed to moved

        //to move or zoom we need to use TransformableNode
        TransformableNode  transformableNode=new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.setParent(anchorNode);
        transformableNode.setRenderable(modelRenderable);
       //adding the node to the scene
        arFragment.getArSceneView().getScene().addChild(anchorNode);
        transformableNode.select();
    }
}

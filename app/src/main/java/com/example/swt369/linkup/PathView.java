package com.example.swt369.linkup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import java.util.LinkedList;

/**
 * Created by swt369 on 2017/8/2.
 */

final class PathView extends View {
    private static Paint paintForPath;
    static {
        paintForPath = new Paint();
        paintForPath.setStrokeWidth(30);
        paintForPath.setColor(Color.CYAN);
        paintForPath.setStyle(Paint.Style.STROKE);
        paintForPath.setStrokeJoin(Paint.Join.ROUND);
        paintForPath.setStrokeCap(Paint.Cap.ROUND);
        paintForPath.setAntiAlias(true);
        paintForPath.setDither(true);
    }
    private Path path;

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path,paintForPath);
    }

    public PathView(Context context, LinkedList<Pair> pathInPixel, final Handler handler) {
        super(context);
        this.path = PathView.listToPath(pathInPixel);

        Animation alpha = AnimationUtils.loadAnimation(getContext(), R.anim.alpha);
        alpha.setFillAfter(true);
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Message message = handler.obtainMessage();
                message.what = Code.CODE_REMOVE_PATH;
                handler.sendMessage(message);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(alpha);
    }

    private static Path listToPath(LinkedList<Pair> pathInPixel){
        Path path = new Path();
        Pair pair = pathInPixel.removeFirst();
        path.moveTo(pair.getRow(),pair.getColumn());
        while (!pathInPixel.isEmpty()){
            pair = pathInPixel.removeFirst();
            path.lineTo(pair.getRow(),pair.getColumn());
        }
        return path;
    }
}

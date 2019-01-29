package com.jimbodev.jimla.projectg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.ArrayList;
import java.util.HashMap;

class Game extends SurfaceView implements Runnable, SurfaceHolder.Callback {

    private Thread gameThread;
    private SurfaceHolder ourHolder;
    private volatile boolean playing, ready;
    public static int WIDTH, HEIGHT;

    final Handler handler = new Handler();
    private boolean selectPointActive = true;
    private boolean longClick = false;
    private boolean continuousPress = false;

    private boolean pointerModeRemove = true;
    private boolean pointerModeSet = false;

    public static int cols, rows;
    public static int gap = 16;
    public static int size = (int) (gap / 2.5f);

    Node startNode, goalNode;
    private int startNodeX, startNodeY;
    private int goalNodeX, goalNodeY;
    ArrayList<ArrayList<Node>> nodes;

    static float RATIO;
    Rect rectBackground;
    Bitmap bitmapBackground;

    ArrayList<Boid> boids;
    Buildable cannon;
    ArrayList<Projectile> projectiles;

    int degress = 0;

    public void init() {
        ourHolder = getHolder();
        ready = false;

        final View view = this;
        view.post(new Runnable() {
            @Override
            public void run() {
                WIDTH = view.getWidth();
                HEIGHT = view.getHeight();

                bitmapBackground = BitmapFactory.decodeResource(
                        getResources(),
                        R.drawable.battlefield1
                );

                //Background bitmap decides ratio
                RATIO = getRatio(bitmapBackground.getWidth(), bitmapBackground.getHeight());
                rectBackground = new Rect(0, 0, (int) (bitmapBackground.getWidth() * RATIO), (int) (bitmapBackground.getHeight() * RATIO));

                createMapNodes();

                boids = new ArrayList<>();
                cannon = new Buildable(0, 0, getResources());
                projectiles = new ArrayList<>();

                ready = true;
            }
        });
    }

    private float getRatio(int w, int h) {
        // Dimension with greatest overhang outside screen decides the RATIO
        if (w - WIDTH > h - HEIGHT) {
            return (float) WIDTH / w;
        }
        else
            return (float) HEIGHT / h;
    }

    private void createMapNodes() {

        cols = getWidth() / gap - 1;
        rows = getHeight() / gap - 1;

        startNodeX = 30;
        startNodeY = 0;

        goalNodeX = 31;
        goalNodeY = (rows - 2);

        nodes = new ArrayList<>();

        for (int i = 0; i < cols; i++) {
            nodes.add(new ArrayList<Node>());
            for (int j = 0; j < rows; j++) {
                nodes.get(i).add(new Node(i, j, (i * Game.gap) + Game.gap, (j * Game.gap) + Game.gap));
            }
        }

        updateNodes();
        dev();
    }

    private void dev() {
        for (ArrayList<Node> a : nodes) {
            for (Node n : a) {
                n.setActive(false);
            }
        }
        startNode.setActive(true);
        goalNode.setActive(true);

        for (int[] a : NavMesh.LEVEL1) {
            nodes.get(a[0]).get(a[1]).setActive(true);
        }
    }

    private void updateNodes() {
        startNode = nodes.get(startNodeX).get(startNodeY);
        goalNode = nodes.get(goalNodeX).get(goalNodeY);

        for (ArrayList<Node> a : nodes) {
            for (Node n : a) {
                if (n.isActive())
                    n.connectNeighbours(nodes);
            }
        }
    }

    private void deactivateNode(int x, int y) {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (nodes.get(i).get(j) != null && Math.hypot(x - (nodes.get(i).get(j).x * gap) - gap, y - (nodes.get(i).get(j).y * gap) - gap) < size * 2) {
                    if (nodes.get(i).get(j) != startNode && nodes.get(i).get(j) != goalNode) {
                        if (!pointerModeSet) {
                            if (nodes.get(i).get(j).isActive()) {
                                pointerModeRemove = true;
                            }
                            else
                                pointerModeRemove = false;

                            pointerModeSet = true;
                        }
                        if (nodes.get(i).get(j).isActive() && pointerModeRemove) {
                            nodes.get(i).get(j).setActive(false);
                        }
                        else if (!pointerModeRemove) {
                            nodes.get(i).get(j).setActive(true);
                        }
                    }
                }
            }
        }
    }

    public void update() {

        for(Boid b : boids)
            b.update(boids);

        if(boids.size() > 0)
            cannon.update(boids.get(boids.size()-1), projectiles);

        for(Projectile p : projectiles)
            p.update();

        if (getButton(MainActivity.B.START_BUTTON) == 1) {
            updateNodes();
            boids.add(new Boid(startNode.getRealX(), startNode.getRealY(), 20, startNode, goalNode));
            resetButton(MainActivity.B.START_BUTTON);
        }
        if (getButton(MainActivity.B.CLEAR_BUTTON) == 1) {

            resetButton(MainActivity.B.CLEAR_BUTTON);
        }
        if (getButton(MainActivity.B.RESTART_BUTTON) == 1) {
            printActiveNodes();
            resetButton(MainActivity.B.RESTART_BUTTON);
        }

        if (getButton(MainActivity.B.SEEK_BAR) > 0) {

        }
    }

    private void printActiveNodes() {
        StringBuilder s = new StringBuilder();
        s.append("{{");
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (nodes.get(i).get(j).isActive()) {
                    s.append(nodes.get(i).get(j).x);
                    s.append(", ");
                    s.append(nodes.get(i).get(j).y);
                    s.append("}, {");
                }
                if (s.toString().length() > 3000) {
                    Log.i("hejsan", s.toString());
                    s = new StringBuilder();
                }
            }
        }
        if (s.toString().length() <= 3000)
            Log.i("hejsan", s.toString());
    }

    public void draw() {
        if (ourHolder.getSurface().isValid()) {
            Canvas canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.LTGRAY);


            Paint paint = new Paint();

            canvas.drawBitmap(bitmapBackground, new Rect(0, 0, bitmapBackground.getWidth(), bitmapBackground.getHeight()), rectBackground, null);

            paint.setARGB(255, 0, 255, 0);
            for(Projectile p : projectiles)
                canvas.drawCircle(p.x, p.y, 20, paint);

            cannon.show(canvas);

            paint.setTextSize(50);
            paint.setStyle(Paint.Style.FILL);
            paint.setARGB(255, 0, 0, 0);

            if (getButton(MainActivity.B.SWITCH) == 1)
                showPathSystem(canvas, paint);

            paint.setARGB(255, 255, 0, 0);
            for(Boid boid : boids) {
                canvas.drawCircle(boid.x, boid.y, boid.getSize(), paint);
                canvas.drawText("Mag: :" + boid.getSpeed(), 10, getHeight() - 10, paint);
            }

            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void showPathSystem(Canvas canvas, Paint paint) {
        for (ArrayList<Node> a : nodes) {
            for (Node n : a) {
                if (n != null)
                    n.show(canvas, paint);
            }
        }
/*        paint.setARGB(255, 255, 0, 0);
        for (Node n : pathfinder.getClosedSet()) {

            if (n != null)
                n.show(canvas, paint);
        }

        paint.setARGB(255, 0, 255, 0);
        for (Node n : pathfinder.getOpenSet()) {
            if (n != null)
                n.show(canvas, paint);
        }

        Node temp = null;
        paint.setARGB(255, 0, 0, 255);
        paint.setStrokeWidth(5);
        for (Node n : pathfinder.getTotal_path()) {
            if (n != null) {
                n.show(canvas, paint);
                if (temp != null)
                    canvas.drawLine(temp.getRealX(), temp.getRealY(), n.getRealX(), n.getRealY(), paint);
                temp = n;

            }
        }*/

        if (startNode != null && goalNode != null) {
            paint.setARGB(255, 255, 255, 0);
            startNode.show(canvas, paint);
            goalNode.show(canvas, paint);
        }
    }

    @Override
    public void run() {

        while (!ready) {
        }

        int FRAMES_PER_SECOND = 100;
        int SKIP_TICKS = 1000 / FRAMES_PER_SECOND;
        long next_game_tick = System.currentTimeMillis();

        long sleep_time = 0;

        while (playing) {
            update();
            draw();

            next_game_tick += SKIP_TICKS;
            sleep_time = next_game_tick - System.currentTimeMillis();
            if (sleep_time >= 0) {
                try {
                    Thread.sleep(sleep_time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private Integer getButton(MainActivity.B b) {
        HashMap<MainActivity.B, Integer> buttonsPressed = MainActivity.buttonsPressed;

        if (buttonsPressed.containsKey(b)
                && buttonsPressed.get(b) != null) {
            return buttonsPressed.get(b);
        }
        else
            return -1;
    }

    private void resetButton(MainActivity.B b) {
        HashMap<MainActivity.B, Integer> buttonsPressed = MainActivity.buttonsPressed;

        buttonsPressed.put(b, 0);
    }

    private void shortPressAction(MotionEvent event) {
        if (selectPointActive) {
            deactivateNode((int) event.getX(), (int) event.getY());
        }
        degress += 5;
    }

    private void longPressAction(MotionEvent event) {

    }

    private void moveAction(MotionEvent event) {
        shortPressAction(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            handler.postDelayed(mLongPressed, ViewConfiguration.getLongPressTimeout());
        }
        if ((event.getAction() == MotionEvent.ACTION_UP)) {
            handler.removeCallbacks(mLongPressed);
            if (longClick) {
                longPressAction(event);
                longClick = false;
            }
            else {
                shortPressAction(event);
            }
            pointerModeSet = false;
            continuousPress = false;
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (continuousPress) {
                moveAction(event);
            }
        }
        return true;
    }

    Runnable mLongPressed = new Runnable() {
        public void run() {
            longClick = true;
            continuousPress = true;
        }
    };


    public void pause() {
        Log.i("info", "pause");
        playing = false;

        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("ERR", "Joining Thread");
        }

    }

    public void resume() {
        Log.i("info", "resume");
        playing = true;

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public Game(Context context) {

        super(context);
        init();
    }

    public Game(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Game(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
}
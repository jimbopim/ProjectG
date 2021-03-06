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

    final static Handler HANDLER = new Handler();
    private boolean selectPointActive = true;
    private boolean longClick = false;

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
    ArrayList<Building> attackers;
    ArrayList<Stationary> mapObstacles;
    Building placedBuilding;
    ArrayList<Projectile> projectiles;

    ConstructionMenu menu;

    ArrayList<Long> FPS = new ArrayList<>();
    long averageFps = 0;
    boolean updateFps = true;
    private int timeBetweenFpsUpdates = 200;

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
                        R.drawable.level1bg
                );

                Stationary.setResources(getResources());

                //Background bitmap decides ratio
                RATIO = getRatio(bitmapBackground.getWidth(), bitmapBackground.getHeight());
                Log.i("hejsan", "RATIO: " + RATIO);
                rectBackground = new Rect(0, 0, (int) (bitmapBackground.getWidth() * RATIO),
                        (int) (bitmapBackground.getHeight() * RATIO));

                Log.i("hejsan", "rectBackground " + rectBackground);

                boids = new ArrayList<>();
                attackers = new ArrayList<>();
                mapObstacles = new ArrayList<>();
                attackers.add(new Cannon(100, 200));
                attackers.add(new ArrowShooter(100, 400));
                mapObstacles.add(new Stationary(600, 500, ObjectType.LOGOBS));
                projectiles = new ArrayList<>();

                createMapNodes();

                menu = new ConstructionMenu(attackers);

                HANDLER.post(updateFpsTimer);

                ready = true;
            }
        });
    }

    private void createNewObjectOnMap() {
        attackers.add(placedBuilding);
        adjustNodes();
    }

    private void adjustNodes() {
        for (Building b : attackers) {
            deleteNodesWithinRect(b.dest);
        }

        for(Stationary s : mapObstacles) {
            deleteNodesWithinRect(s.dest);
        }
    }

    private float getRatio(int w, int h) {
        // Shortest side decides ratio
        if (w - WIDTH < h - HEIGHT) {
            Log.i("hejsan", "WIDTH: " + WIDTH + " HEIGHT: " + HEIGHT);
            return (float) WIDTH / w;
        }
        else {
            Log.i("hejsan", "WIDTH111: " + WIDTH + " HEIGHT111: " + HEIGHT);
            return (float) HEIGHT / h;
        }
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
                nodes.get(i).add(new Node(i, j, (i * Game.gap) + Game.gap,
                        (j * Game.gap) + Game.gap));
            }
        }

        startNode = nodes.get(startNodeX).get(startNodeY);
        goalNode = nodes.get(goalNodeX).get(goalNodeY);

        updateNodes();
        createNavMesh();
    }

    private void updateNodes() {
        for (ArrayList<Node> a : nodes) {
            for (Node n : a) {
                if (n.isActive())
                    n.connectNeighbours(nodes);
            }
        }
    }

    private void createNavMesh() {
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

    private void deleteNodesWithinRect(Rect r) {
        Log.i("hejsan", "delete");
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (nodes.get(i).get(j) != null && r.contains((int)nodes.get(i).get(j).getRealX(), (int) nodes.get(i).get(j).getRealY())){
                    if (nodes.get(i).get(j) != startNode && nodes.get(i).get(j) != goalNode) {
                        if (nodes.get(i).get(j).isActive()) {
                            nodes.get(i).get(j).setActive(false);

                        }
                    }
                }
            }
        }
    }

    private void switchNode(int x, int y) {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (nodes.get(i).get(j) != null && Math.hypot(x - (nodes.get(i).get(j).x * gap) - gap, y - (nodes.get(i).get(j).y * gap) - gap) < size * 2) {
                    if (nodes.get(i).get(j) != startNode && nodes.get(i).get(j) != goalNode) {
                        if (!pointerModeSet) {
                            pointerModeRemove = nodes.get(i).get(j).isActive();

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


        if(placedBuilding != null) {
            Log.i("hejsan", "kov");
            createNewObjectOnMap();
            placedBuilding = null;
        }

        ArrayList<Vector> remove = new ArrayList<>();

        for (Projectile p : projectiles) {
            for (Boid b : boids) {
                if (b.checkCollision(p, p.getWidth())) {
                    //b.changeHealth(-100);
                    //p.setRemovable(false);
                    p.hit();
                    b.hit(p);
                    if (b.isRemovable())
                        remove.add(b);
                    remove.add(p);
                }
            }
        }
        boids.removeAll(remove);
        projectiles.removeAll(remove);


        for (Boid b : boids)
            b.update(boids);

        if (boids.size() > 0) {
            for (Building b : attackers) {
                b.update(boids.get(boids.size() - 1));
                if (b instanceof Attacker)
                    ((SimpleAttacker) b).shoot(boids.get(boids.size() - 1), projectiles);
            }
        }

        for (Projectile p : projectiles)
            p.update();

        if (getButton(MainActivity.B.START_BUTTON) == 1) {
            updateNodes();
            boids.add(new Boid(startNode.getRealX(), startNode.getRealY(), ObjectType.BOID, startNode, goalNode));
            resetButton(MainActivity.B.START_BUTTON);
        }
        if (getButton(MainActivity.B.CLEAR_BUTTON) == 1) {
            toggleMenu();
            resetButton(MainActivity.B.CLEAR_BUTTON);
        }
        if (getButton(MainActivity.B.RESTART_BUTTON) == 1) {
            printActiveNodes();
            resetButton(MainActivity.B.RESTART_BUTTON);
        }

        if (getButton(MainActivity.B.SEEK_BAR) > 0) {

        }
    }

    private void toggleMenu() {
        menu.setActive(!menu.isActive());
        selectPointActive = !selectPointActive;
    }

    public void draw() {
        if (ourHolder.getSurface().isValid()) {
            Canvas canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.LTGRAY);


            Paint paint = new Paint();

            canvas.drawBitmap(bitmapBackground, new Rect(0, 0, bitmapBackground.getWidth(),
                    bitmapBackground.getHeight()), rectBackground, null);

            paint.setARGB(255, 0, 0, 0);
            for (Projectile p : projectiles)
                p.show(canvas);

            for (Building b : attackers)
                b.show(canvas);

            for (Stationary s : mapObstacles) {
                s.show(canvas);
                //canvas.drawRect(s.dest, paint);
            }

            paint.setTextSize(50);
            paint.setStyle(Paint.Style.FILL);
            paint.setARGB(255, 0, 0, 0);

            if (getButton(MainActivity.B.SWITCH) == 1)
                showPathSystem(canvas, paint);

            paint.setARGB(255, 255, 0, 0);
            for (Boid boid : boids) {
                canvas.drawCircle(boid.x, boid.y, boid.getWidth(), paint);
            }

            menu.show(canvas);

            canvas.drawText("" + averageFps, 10, getHeight() - 10, paint);

            ourHolder.unlockCanvasAndPost(canvas);
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

    private void showPathSystem(Canvas canvas, Paint paint) {
        for (ArrayList<Node> a : nodes) {
            for (Node n : a) {
                if (n != null)
                    n.show(canvas, paint);
            }
        }

        if (startNode != null && goalNode != null) {
            paint.setARGB(255, 255, 255, 0);
            startNode.show(canvas, paint);
            goalNode.show(canvas, paint);
        }
    }

    void calculateFPS(long prevTime) {
        FPS.add(1000 / (System.currentTimeMillis() - prevTime));

        if (updateFps) {
            averageFps = 0;
            for (long l : FPS)
                averageFps += l;

            averageFps = averageFps / FPS.size();

            FPS.clear();
            updateFps = false;
        }
    }

    @Override
    public void run() {

        while (!ready) {
        }

        int FRAMES_PER_SECOND = 60; //60 är vanligtvis max
        int SKIP_TICKS = 1000 / FRAMES_PER_SECOND;
        long next_game_tick = System.currentTimeMillis();
        long prevTime = 0;

        long sleep_time = 0;

        while (playing) {
            update();
            draw();

            calculateFPS(prevTime);
            prevTime = System.currentTimeMillis();

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

    private void downAction(MotionEvent event) {
        if (menu.isActive()) {
            menu.actionDown(event);
        }
    }

    private void upAction(MotionEvent event) {
        pointerModeSet = false;
    }

    private void shortPressAction(MotionEvent event) {
        if (selectPointActive) {
            switchNode((int) event.getX(), (int) event.getY());
        }
        if (menu.isActive()) {
            placedBuilding = menu.actionUp(event);
        }
    }

    private void longPressAction(MotionEvent event) {

    }

    private void moveAction(MotionEvent event) {
        if (selectPointActive) {
            shortPressAction(event);
        }
        if (menu.isActive()) {
            menu.actionMove(event);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            HANDLER.postDelayed(mLongPressed, ViewConfiguration.getLongPressTimeout());
            downAction(event);
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            HANDLER.removeCallbacks(mLongPressed);
            upAction(event);
            if (longClick) {
                longPressAction(event);
                longClick = false;
            }
            else {
                shortPressAction(event);
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            HANDLER.removeCallbacks(mLongPressed);
            longClick = false;
            moveAction(event);
        }
        return true;
    }

    Runnable mLongPressed = new Runnable() {
        @Override
        public void run() {
            longClick = true;
        }
    };

    Runnable updateFpsTimer = new Runnable() {
        @Override
        public void run() {
            updateFps = true;
            HANDLER.postDelayed(updateFpsTimer, timeBetweenFpsUpdates);
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
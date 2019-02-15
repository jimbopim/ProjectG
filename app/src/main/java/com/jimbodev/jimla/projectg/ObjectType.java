package com.jimbodev.jimla.projectg;

class ObjectType {
    final static int FRAMEWIDTH = 64, FRAMEHEIGHT = 64;

    final static cCANNON CANNON = new cCANNON();
    final static cARROWSHOOTER ARROWSHOOTER = new cARROWSHOOTER();
    final static cCANNONBALL CANNONBALL = new cCANNONBALL();
    final static cBOID BOID = new cBOID();

    private static class cBOID implements Movable, Sprite {
        @Override
        public int getSize() {
            return 20;
        }

        @Override
        public float getMaxSpeed() {
            return 10f;
        }

        @Override
        public float getMaxForce() {
            return 5f;
        }
    }

    private static class cCANNON implements Tower, Bitmap{
        @Override
        public int getFireframes() {
            return 1;
        }

        @Override
        public int getLayer1Col() {
            return 0;
        }

        @Override
        public int getLayer1Row() {
            return 0;
        }

        @Override
        public int getLayer2Col() {
            return 0;
        }

        @Override
        public int getLayer2Row() {
            return 1;
        }

        @Override
        public int getScale() {
            return 2;
        }

        @Override
        public int getSize() {
            return 0;
        }
    }

    private static class cARROWSHOOTER implements Tower, Bitmap{
        @Override
        public int getFireframes() {
            return 2;
        }

        @Override
        public int getLayer1Col() {
            return 1;
        }

        @Override
        public int getLayer1Row() {
            return 0;
        }

        @Override
        public int getLayer2Col() {
            return 1;
        }

        @Override
        public int getLayer2Row() {
            return 1;
        }

        @Override
        public int getScale() {
            return 2;
        }

        @Override
        public int getSize() {
            return 0;
        }
    }

    private static class cCANNONBALL implements Movable, Sprite{
        @Override
        public int getSize() {
            return 10;
        }

        @Override
        public float getMaxSpeed() {
            return 20;
        }

        @Override
        public float getMaxForce() {
            return 10;
        }
    }

    interface Tower {
        int getFireframes();
    }

    interface Bitmap extends Sprite{
        int getLayer1Col();
        int getLayer1Row();
        int getLayer2Col();
        int getLayer2Row();
        int getScale();
    }

    interface Sprite{
        int getSize();
    }


    interface Movable {
        float getMaxSpeed();
        float getMaxForce();
    }


}

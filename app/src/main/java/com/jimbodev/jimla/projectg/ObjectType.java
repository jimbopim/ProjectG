package com.jimbodev.jimla.projectg;

class ObjectType {
    final static int FRAMEWIDTH = 64, FRAMEHEIGHT = 64;

    final static cLogObs LOGOBS = new cLogObs();
    final static cCANNON CANNON = new cCANNON();
    final static cARROWSHOOTER ARROWSHOOTER = new cARROWSHOOTER();
    final static cCANNONBALL CANNONBALL = new cCANNONBALL();
    final static cBOID BOID = new cBOID();

    private static class cLogObs implements Obstacle, Bitmap {

        @Override
        public int[] getLayer1Coord() {
            return new int[]{4, 0};
        }

        @Override
        public int[] getLayer2Coord() {
            return null;
        }

        @Override
        public int getScale() {
            return 2;
        }
    }

    private static class cBOID implements Movable, Paintable {
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

    private static class cCANNON implements Tower, Bitmap {
        @Override
        public int getFireframes() {
            return 1;
        }

        @Override
        public int getRecoil() {
            return 20;
        }

        @Override
        public int[] getLayer1Coord() {
            return new int[]{0, 0};
        }

        @Override
        public int[] getLayer2Coord() {
            return new int[]{0, 1};
        }

        @Override
        public int getScale() {
            return 2;
        }
    }

    private static class cARROWSHOOTER implements Tower, Bitmap {
        @Override
        public int getFireframes() {
            return 2;
        }

        @Override
        public int getRecoil() {
            return 0;
        }

        @Override
        public int[] getLayer1Coord() {
            return new int[]{1, 0};
        }

        @Override
        public int[] getLayer2Coord() {
            return new int[]{1, 1};
        }

        @Override
        public int getScale() {
            return 2;
        }
    }

    private static class cCANNONBALL implements Movable, Paintable {
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

    interface Obstacle {

    }

    interface Tower {
        int getFireframes();
        int getRecoil();
    }

    interface Movable {
        float getMaxSpeed();
        float getMaxForce();
    }

    interface Bitmap extends Drawable {
        int[] getLayer1Coord();
        int[] getLayer2Coord();
        int getScale();
    }

    interface Paintable extends Drawable {
        int getSize();
    }

    interface Drawable { }

}

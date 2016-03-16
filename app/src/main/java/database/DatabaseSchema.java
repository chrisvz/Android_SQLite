package database;

import java.util.UUID;

/**
 * Created by Chris on 3/15/2016.
 */
public class DatabaseSchema {


    public static final class USER  {
        public static final String NAME = "user";

        public static final class COLS {
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String AGE = "age";
            public static final String isMARRIED = "isMarried";
        }
    }
}

package metamorph.warehouse;

public enum RequestContentType {
	MULTIPART(Type.MULTIPART),
	NORMAL(Type.NORMAL);

    public class Type{
        public static final int MULTIPART = 1;
        public static final int NORMAL = 0;
    }

    private final int type;

    private RequestContentType(int type) {
        this.type = type;
    }
}

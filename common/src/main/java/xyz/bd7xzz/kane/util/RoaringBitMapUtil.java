package xyz.bd7xzz.kane.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.KryoDataInput;
import com.esotericsoftware.kryo.io.KryoDataOutput;
import com.esotericsoftware.kryo.io.Output;
import org.roaringbitmap.RoaringBitmap;
import org.roaringbitmap.buffer.MutableRoaringBitmap;
import org.roaringbitmap.longlong.Roaring64Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author baodi1
 * @description: TODO
 * @date 2021/8/25 6:24 下午
 */
public class RoaringBitMapUtil {
    private RoaringBitmap roaringBitmap;
    private MutableRoaringBitmap mutableRoaringBitmap;
    private Roaring64Bitmap longBitmapDataProvider;

    private RoaringBitMapUtil(MutableRoaringBitmap mutableRoaringBitmap) {
        this.mutableRoaringBitmap = mutableRoaringBitmap;
    }

    private RoaringBitMapUtil(RoaringBitmap roaringBitmap) {
        this.roaringBitmap = roaringBitmap;
    }

    private RoaringBitMapUtil(Roaring64Bitmap longBitmapDataProvider) {
        this.longBitmapDataProvider = longBitmapDataProvider;
    }

    public static RoaringBitMapUtil mappedRoaringBitmap() {
        return new RoaringBitMapUtil(new MutableRoaringBitmap());
    }

    public static RoaringBitMapUtil roaringBitMap() {
        return new RoaringBitMapUtil(new RoaringBitmap());
    }

    public static RoaringBitMapUtil longRoaringBitMap() {
        return new RoaringBitMapUtil(new Roaring64Bitmap());
    }

    public static RoaringBitMapUtil mappedRoaringBitmap(byte[] bytes) {
        checkBytes(bytes);
        Kryo kryo = newKryo(MutableRoaringBitmap.class);
        try (Input input = new Input(bytes)) {
            return new RoaringBitMapUtil(kryo.readObject(input, MutableRoaringBitmap.class));
        }
    }

    public static RoaringBitMapUtil roaringBitMap(byte[] bytes) {
        checkBytes(bytes);
        Kryo kryo = newKryo(RoaringBitmap.class);
        try (Input input = new Input(bytes)) {
            return new RoaringBitMapUtil(kryo.readObject(input, RoaringBitmap.class));
        }
    }

    public static RoaringBitMapUtil longRoaringBitMap(byte[] bytes) {
        checkBytes(bytes);
        Kryo kryo = newKryo(Roaring64Bitmap.class);
        try (Input input = new Input(bytes)) {
            return new RoaringBitMapUtil(kryo.readObject(input, Roaring64Bitmap.class));
        }
    }

    private static void checkBytes(byte[] bytes) {
        if (bytes == null || bytes.length <= 0) {
            throw new IllegalArgumentException();
        }
    }


    public RoaringBitMapUtil add(int... number) {
        if (null != roaringBitmap) {
            roaringBitmap.add(number);
            return this;
        }
        if (null != mutableRoaringBitmap) {
            roaringBitmap.add(number);
            return this;
        }
        throw new IllegalStateException();
    }

    public RoaringBitMapUtil add(long... number) {
        if (null != longBitmapDataProvider) {
            longBitmapDataProvider.add(number);
            return this;
        }
        throw new IllegalStateException();
    }

    public boolean contains(int number) {
        if (null != roaringBitmap) {
            return roaringBitmap.contains(number);
        }
        if (null != mutableRoaringBitmap) {
            return mutableRoaringBitmap.contains(number);
        }
        throw new IllegalStateException();
    }

    public boolean contains(long number) {
        if (null != longBitmapDataProvider) {
            return longBitmapDataProvider.contains(number);
        }
        throw new IllegalStateException();
    }

    public RoaringBitMapUtil remove(int number) {
        if (null != roaringBitmap) {
            roaringBitmap.remove(number);
            return this;
        }
        if (null != mutableRoaringBitmap) {
            mutableRoaringBitmap.remove(number);
            return this;
        }
        throw new IllegalStateException();
    }

    public RoaringBitMapUtil remove(long number) {
        if (null != longBitmapDataProvider) {
            longBitmapDataProvider.removeLong(number);
            return this;
        }
        throw new IllegalStateException();
    }

    public void clear() {
        if (null != roaringBitmap) {
            roaringBitmap.clear();
            roaringBitmap = null;
        }
        if (null != mutableRoaringBitmap) {
            mutableRoaringBitmap.clear();
            mutableRoaringBitmap = null;
        }
        if (null != longBitmapDataProvider) {
            longBitmapDataProvider.clear();
            longBitmapDataProvider = null;
        }
    }

    public byte[] writeToByteArray() throws IOException {
        Kryo kryo;
        Serializer serializer;
        if (null != roaringBitmap) {
            serializer = new RoaringSerializer();
            kryo = newKryo(RoaringBitmap.class);
        } else if (null != mutableRoaringBitmap) {
            serializer = new MutableRoaringBitmapSerializer();
            kryo = newKryo(MutableRoaringBitmap.class);
        } else if (null != longBitmapDataProvider) {
            serializer = new LongRoaringBitMapSerializer();
            kryo = newKryo(Roaring64Bitmap.class);
        } else {
            throw new IllegalStateException();
        }

        ByteArrayOutputStream byteArrayOutputStream = null;
        try (Output output = new Output(byteArrayOutputStream)) {
            byteArrayOutputStream = new ByteArrayOutputStream();
            kryo.writeObject(output, serializer);
            return byteArrayOutputStream.toByteArray();
        } finally {
            if (null != byteArrayOutputStream) {
                byteArrayOutputStream.close();
            }
        }
    }

    private static Kryo newKryo(Class<?> clazz) {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        kryo.register(clazz);
        return kryo;
    }

    private class RoaringSerializer extends Serializer<RoaringBitmap> {
        @Override
        public void write(Kryo kryo, Output output, RoaringBitmap bitmap) {
            try {
                bitmap.serialize(new KryoDataOutput(output));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public RoaringBitmap read(Kryo kryo, Input input, Class<? extends RoaringBitmap> type) {
            RoaringBitmap bitmap = new RoaringBitmap();
            try {
                bitmap.deserialize(new KryoDataInput(input));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return bitmap;
        }
    }

    private class MutableRoaringBitmapSerializer extends Serializer<MutableRoaringBitmap> {
        @Override
        public void write(Kryo kryo, Output output, MutableRoaringBitmap bitmap) {
            try {
                bitmap.serialize(new KryoDataOutput(output));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public MutableRoaringBitmap read(Kryo kryo, Input input, Class<? extends MutableRoaringBitmap> type) {
            MutableRoaringBitmap bitmap = new MutableRoaringBitmap();
            try {
                bitmap.deserialize(new KryoDataInput(input));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return bitmap;
        }
    }

    private class LongRoaringBitMapSerializer extends Serializer<Roaring64Bitmap> {
        @Override
        public void write(Kryo kryo, Output output, Roaring64Bitmap bitmap) {
            try {
                bitmap.serialize(new KryoDataOutput(output));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Roaring64Bitmap read(Kryo kryo, Input input, Class<? extends Roaring64Bitmap> type) {
            Roaring64Bitmap bitmap = new Roaring64Bitmap();
            try {
                bitmap.deserialize(new KryoDataInput(input));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return bitmap;
        }
    }
}

package com.bobmowzie.mowziesmobs.client.particle.util;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.math.Vec3d;

public class RibbonParticleData extends AdvancedParticleData {
    public static final Factory<RibbonParticleData> DESERIALIZER = new Factory<RibbonParticleData>() {
        public RibbonParticleData read(ParticleType<RibbonParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            double airDrag = reader.readDouble();
            reader.expect(' ');
            double red = reader.readDouble();
            reader.expect(' ');
            double green = reader.readDouble();
            reader.expect(' ');
            double blue = reader.readDouble();
            reader.expect(' ');
            double alpha = reader.readDouble();
            reader.expect(' ');
            String rotationMode = reader.readString();
            reader.expect(' ');
            double scale = reader.readDouble();
            reader.expect(' ');
            double yaw = reader.readDouble();
            reader.expect(' ');
            double pitch = reader.readDouble();
            reader.expect(' ');
            double roll = reader.readDouble();
            reader.expect(' ');
            boolean emissive = reader.readBoolean();
            reader.expect(' ');
            double duration = reader.readDouble();
            reader.expect(' ');
            reader.readDouble();
            reader.expect(' ');
            int length = reader.readInt();
            ParticleRotation rotation;
            if (rotationMode.equals("face_camera")) rotation = new ParticleRotation.FaceCamera((float) 0);
            else if (rotationMode.equals("euler"))
                rotation = new ParticleRotation.EulerAngles((float) yaw, (float) pitch, (float) roll);
            else rotation = new ParticleRotation.OrientVector(new Vec3d(yaw, pitch, roll));
            return new RibbonParticleData(particleTypeIn, rotation, scale, red, green, blue, alpha, airDrag, duration, emissive, length);
        }

        public RibbonParticleData read(ParticleType<RibbonParticleData> particleTypeIn, PacketByteBuf buffer) {
            double airDrag = buffer.readFloat();
            double red = buffer.readFloat();
            double green = buffer.readFloat();
            double blue = buffer.readFloat();
            double alpha = buffer.readFloat();
            String rotationMode = buffer.readString();
            double scale = buffer.readFloat();
            double yaw = buffer.readFloat();
            double pitch = buffer.readFloat();
            double roll = buffer.readFloat();
            boolean emissive = buffer.readBoolean();
            double duration = buffer.readFloat();
            buffer.readFloat();
            int length = buffer.readInt();
            ParticleRotation rotation;
            if (rotationMode.equals("face_camera")) rotation = new ParticleRotation.FaceCamera((float) 0);
            else if (rotationMode.equals("euler"))
                rotation = new ParticleRotation.EulerAngles((float) yaw, (float) pitch, (float) roll);
            else rotation = new ParticleRotation.OrientVector(new Vec3d(yaw, pitch, roll));
            return new RibbonParticleData(particleTypeIn, rotation, scale, red, green, blue, alpha, airDrag, duration, emissive, length);
        }
    };

    private final int length;

    public RibbonParticleData(ParticleType<? extends RibbonParticleData> type, ParticleRotation rotation, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, int length) {
        this(type, rotation, scale, r, g, b, a, drag, duration, emissive, length, new ParticleComponent[]{});
    }

    public RibbonParticleData(ParticleType<? extends RibbonParticleData> type, ParticleRotation rotation, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, int length, ParticleComponent[] components) {
        super(type, rotation, scale, r, g, b, a, drag, duration, emissive, false, components);
        this.length = length;
    }

    public static Codec<RibbonParticleData> CODEC_RIBBON(ParticleType<RibbonParticleData> particleType) {
        return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                        Codec.DOUBLE.fieldOf("scale").forGetter(RibbonParticleData::getScale),
                        Codec.DOUBLE.fieldOf("r").forGetter(RibbonParticleData::getRed),
                        Codec.DOUBLE.fieldOf("g").forGetter(RibbonParticleData::getGreen),
                        Codec.DOUBLE.fieldOf("b").forGetter(RibbonParticleData::getBlue),
                        Codec.DOUBLE.fieldOf("a").forGetter(RibbonParticleData::getAlpha),
                        Codec.DOUBLE.fieldOf("drag").forGetter(RibbonParticleData::getAirDrag),
                        Codec.DOUBLE.fieldOf("duration").forGetter(RibbonParticleData::getDuration),
                        Codec.BOOL.fieldOf("emissive").forGetter(RibbonParticleData::isEmissive),
                        Codec.INT.fieldOf("length").forGetter(RibbonParticleData::getLength)
                ).apply(codecBuilder, (scale, r, g, b, a, drag, duration, emissive, length) ->
                        new RibbonParticleData(particleType, new ParticleRotation.FaceCamera(0), scale, r, g, b, a, drag, duration, emissive, length, new ParticleComponent[]{}))
        );
    }

    @Override
    public void write(PacketByteBuf buffer) {
        super.write(buffer);
        buffer.writeInt(this.length);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String asString() {
        return super.asString() + " " + this.length;
    }

    @Environment(EnvType.CLIENT)
    public int getLength() {
        return this.length;
    }
}

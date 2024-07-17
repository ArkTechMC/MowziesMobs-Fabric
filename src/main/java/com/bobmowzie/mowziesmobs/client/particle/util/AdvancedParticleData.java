package com.bobmowzie.mowziesmobs.client.particle.util;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.Vec3d;

import java.util.Locale;

public class AdvancedParticleData implements ParticleEffect {
    public static final Factory<AdvancedParticleData> DESERIALIZER = new Factory<AdvancedParticleData>() {
        public AdvancedParticleData read(ParticleType<AdvancedParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
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
            double faceCameraAngle = reader.readDouble();
            reader.expect(' ');
            boolean canCollide = reader.readBoolean();
            ParticleRotation rotation;
            if (rotationMode.equals("face_camera")) rotation = new ParticleRotation.FaceCamera((float) faceCameraAngle);
            else if (rotationMode.equals("euler"))
                rotation = new ParticleRotation.EulerAngles((float) yaw, (float) pitch, (float) roll);
            else rotation = new ParticleRotation.OrientVector(new Vec3d(yaw, pitch, roll));
            return new AdvancedParticleData(particleTypeIn, rotation, scale, red, green, blue, alpha, airDrag, duration, emissive, canCollide);
        }

        public AdvancedParticleData read(ParticleType<AdvancedParticleData> particleTypeIn, PacketByteBuf buffer) {
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
            double faceCameraAngle = buffer.readFloat();
            boolean canCollide = buffer.readBoolean();
            ParticleRotation rotation;
            if (rotationMode.equals("face_camera")) rotation = new ParticleRotation.FaceCamera((float) faceCameraAngle);
            else if (rotationMode.equals("euler"))
                rotation = new ParticleRotation.EulerAngles((float) yaw, (float) pitch, (float) roll);
            else rotation = new ParticleRotation.OrientVector(new Vec3d(yaw, pitch, roll));
            return new AdvancedParticleData(particleTypeIn, rotation, scale, red, green, blue, alpha, airDrag, duration, emissive, canCollide);
        }
    };

    private final ParticleType<? extends AdvancedParticleData> type;

    private final float airDrag;
    private final float red, green, blue, alpha;
    private final ParticleRotation rotation;
    private final float scale;
    private final boolean emissive;
    private final float duration;
    private final boolean canCollide;

    private final ParticleComponent[] components;

    public AdvancedParticleData(ParticleType<? extends AdvancedParticleData> type, ParticleRotation rotation, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, boolean canCollide) {
        this(type, rotation, scale, r, g, b, a, drag, duration, emissive, canCollide, new ParticleComponent[]{});
    }

    public AdvancedParticleData(ParticleType<? extends AdvancedParticleData> type, ParticleRotation rotation, double scale, double r, double g, double b, double a, double drag, double duration, boolean emissive, boolean canCollide, ParticleComponent[] components) {
        this.type = type;

        this.rotation = rotation;

        this.scale = (float) scale;

        this.red = (float) r;
        this.green = (float) g;
        this.blue = (float) b;
        this.alpha = (float) a;
        this.emissive = emissive;

        this.airDrag = (float) drag;

        this.duration = (float) duration;

        this.canCollide = canCollide;

        this.components = components;
    }

    public static Codec<AdvancedParticleData> CODEC(ParticleType<AdvancedParticleData> particleType) {
        return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                        Codec.DOUBLE.fieldOf("scale").forGetter(AdvancedParticleData::getScale),
                        Codec.DOUBLE.fieldOf("r").forGetter(AdvancedParticleData::getRed),
                        Codec.DOUBLE.fieldOf("g").forGetter(AdvancedParticleData::getGreen),
                        Codec.DOUBLE.fieldOf("b").forGetter(AdvancedParticleData::getBlue),
                        Codec.DOUBLE.fieldOf("a").forGetter(AdvancedParticleData::getAlpha),
                        Codec.DOUBLE.fieldOf("drag").forGetter(AdvancedParticleData::getAirDrag),
                        Codec.DOUBLE.fieldOf("duration").forGetter(AdvancedParticleData::getDuration),
                        Codec.BOOL.fieldOf("emissive").forGetter(AdvancedParticleData::isEmissive),
                        Codec.BOOL.fieldOf("canCollide").forGetter(AdvancedParticleData::getCanCollide)
                ).apply(codecBuilder, (scale, r, g, b, a, drag, duration, emissive, canCollide) ->
                        new AdvancedParticleData(particleType, new ParticleRotation.FaceCamera(0), scale, r, g, b, a, drag, duration, emissive, canCollide, new ParticleComponent[]{}))
        );
    }

    @Override
    public void write(PacketByteBuf buffer) {
        String rotationMode;
        float faceCameraAngle = 0;
        float yaw = 0;
        float pitch = 0;
        float roll = 0;
        if (this.rotation instanceof ParticleRotation.FaceCamera) {
            rotationMode = "face_camera";
            faceCameraAngle = ((ParticleRotation.FaceCamera) this.rotation).faceCameraAngle;
        } else if (this.rotation instanceof ParticleRotation.EulerAngles) {
            rotationMode = "euler";
            yaw = ((ParticleRotation.EulerAngles) this.rotation).yaw;
            pitch = ((ParticleRotation.EulerAngles) this.rotation).pitch;
            roll = ((ParticleRotation.EulerAngles) this.rotation).roll;
        } else {
            rotationMode = "orient";
            Vec3d vec = ((ParticleRotation.OrientVector) this.rotation).orientation;
            yaw = (float) vec.x;
            pitch = (float) vec.y;
            roll = (float) vec.z;
        }

        buffer.writeFloat(this.airDrag);
        buffer.writeFloat(this.red);
        buffer.writeFloat(this.green);
        buffer.writeFloat(this.blue);
        buffer.writeFloat(this.alpha);
        buffer.writeString(rotationMode);
        buffer.writeFloat(this.scale);
        buffer.writeFloat(yaw);
        buffer.writeFloat(pitch);
        buffer.writeFloat(roll);
        buffer.writeBoolean(this.emissive);
        buffer.writeFloat(this.duration);
        buffer.writeFloat(faceCameraAngle);
        buffer.writeBoolean(this.canCollide);
    }

    @Override
    public String asString() {
        String rotationMode;
        float faceCameraAngle = 0;
        float yaw = 0;
        float pitch = 0;
        float roll = 0;
        if (this.rotation instanceof ParticleRotation.FaceCamera) {
            rotationMode = "face_camera";
            faceCameraAngle = ((ParticleRotation.FaceCamera) this.rotation).faceCameraAngle;
        } else if (this.rotation instanceof ParticleRotation.EulerAngles) {
            rotationMode = "euler";
            yaw = ((ParticleRotation.EulerAngles) this.rotation).yaw;
            pitch = ((ParticleRotation.EulerAngles) this.rotation).pitch;
            roll = ((ParticleRotation.EulerAngles) this.rotation).roll;
        } else {
            rotationMode = "orient";
            Vec3d vec = ((ParticleRotation.OrientVector) this.rotation).orientation;
            yaw = (float) vec.x;
            pitch = (float) vec.y;
            roll = (float) vec.z;
        }

        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %s %.2f %.2f %.2f %.2f %b %.2f %.2f %b", Registries.PARTICLE_TYPE.getId(this.getType()),
                this.airDrag, this.red, this.green, this.blue, this.alpha, rotationMode, this.scale, yaw, pitch, roll, this.emissive, this.duration, faceCameraAngle, this.canCollide);
    }

    @Override
    public ParticleType<? extends AdvancedParticleData> getType() {
        return this.type;
    }

    @Environment(EnvType.CLIENT)
    public double getRed() {
        return this.red;
    }

    @Environment(EnvType.CLIENT)
    public double getGreen() {
        return this.green;
    }

    @Environment(EnvType.CLIENT)
    public double getBlue() {
        return this.blue;
    }

    @Environment(EnvType.CLIENT)
    public double getAlpha() {
        return this.alpha;
    }

    @Environment(EnvType.CLIENT)
    public double getAirDrag() {
        return this.airDrag;
    }

    @Environment(EnvType.CLIENT)
    public ParticleRotation getRotation() {
        return this.rotation;
    }

    @Environment(EnvType.CLIENT)
    public double getScale() {
        return this.scale;
    }

    @Environment(EnvType.CLIENT)
    public boolean isEmissive() {
        return this.emissive;
    }

    @Environment(EnvType.CLIENT)
    public double getDuration() {
        return this.duration;
    }

    @Environment(EnvType.CLIENT)
    public boolean getCanCollide() {
        return this.canCollide;
    }

    @Environment(EnvType.CLIENT)
    public ParticleComponent[] getComponents() {
        return this.components;
    }
}

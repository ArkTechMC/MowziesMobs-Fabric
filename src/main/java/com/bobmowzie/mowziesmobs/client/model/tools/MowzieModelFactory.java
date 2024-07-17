package com.bobmowzie.mowziesmobs.client.model.tools;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieGeoBone;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.cache.object.GeoQuad;
import software.bernie.geckolib.loading.json.raw.Bone;
import software.bernie.geckolib.loading.json.raw.Cube;
import software.bernie.geckolib.loading.json.raw.ModelProperties;
import software.bernie.geckolib.loading.object.BakedModelFactory;
import software.bernie.geckolib.loading.object.BoneStructure;
import software.bernie.geckolib.loading.object.GeometryTree;
import software.bernie.geckolib.util.RenderUtils;

import java.util.List;

public class MowzieModelFactory implements BakedModelFactory {

    @Override
    public BakedGeoModel constructGeoModel(GeometryTree geometryTree) {
        List<GeoBone> bones = new ObjectArrayList<>();

        for (BoneStructure boneStructure : geometryTree.topLevelBones().values()) {
            bones.add(this.constructBone(boneStructure, geometryTree.properties(), null));
        }

        return new BakedGeoModel(bones, geometryTree.properties());
    }

    @Override
    public GeoBone constructBone(BoneStructure boneStructure, ModelProperties properties, @Nullable GeoBone parent) {
        Bone bone = boneStructure.self();
        MowzieGeoBone newBone = new MowzieGeoBone(parent, bone.name(), bone.mirror(), bone.inflate(), bone.neverRender(), bone.reset());
        Vec3d rotation = RenderUtils.arrayToVec(bone.rotation());
        Vec3d pivot = RenderUtils.arrayToVec(bone.pivot());

        newBone.updateRotation((float) Math.toRadians(-rotation.x), (float) Math.toRadians(-rotation.y), (float) Math.toRadians(rotation.z));
        newBone.updatePivot((float) -pivot.x, (float) pivot.y, (float) pivot.z);

        for (Cube cube : bone.cubes()) {
            newBone.getCubes().add(this.constructCube(cube, properties, newBone));
        }

        for (BoneStructure child : boneStructure.children().values()) {
            newBone.getChildBones().add(this.constructBone(child, properties, newBone));
        }

        return newBone;
    }

    @Override
    public GeoCube constructCube(Cube cube, ModelProperties properties, GeoBone bone) {
        boolean mirror = cube.mirror() == Boolean.TRUE;
        double inflate = cube.inflate() != null ? cube.inflate() / 16f : (bone.getInflate() == null ? 0 : bone.getInflate() / 16f);
        Vec3d size = RenderUtils.arrayToVec(cube.size());
        Vec3d origin = RenderUtils.arrayToVec(cube.origin());
        Vec3d rotation = RenderUtils.arrayToVec(cube.rotation());
        Vec3d pivot = RenderUtils.arrayToVec(cube.pivot());
        origin = new Vec3d(-(origin.x + size.x) / 16d, origin.y / 16d, origin.z / 16d);
        Vec3d vertexSize = size.multiply(1 / 16d, 1 / 16d, 1 / 16d);

        pivot = pivot.multiply(-1, 1, 1);
        rotation = new Vec3d(Math.toRadians(-rotation.x), Math.toRadians(-rotation.y), Math.toRadians(rotation.z));
        GeoQuad[] quads = this.buildQuads(cube.uv(), new VertexSet(origin, vertexSize, inflate), cube, (float) properties.textureWidth(), (float) properties.textureHeight(), mirror);

        return new GeoCube(quads, pivot, rotation, size, inflate, mirror);
    }
}

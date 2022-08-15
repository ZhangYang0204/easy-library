package pers.zhangyang.easylibrary.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;
import pers.zhangyang.easylibrary.EasyPlugin;
import pers.zhangyang.easylibrary.exception.NotSameWorldLocationException;

import java.util.ArrayList;
import java.util.List;

public class BlockEffectUtil {

    //用基岩显示选区，基岩不会被破坏，方便一些
    public static void showSection(Location location1,Location location2) throws NotSameWorldLocationException {

        if (location1.getWorld()==null||location2.getWorld()==null){
            throw new NotSameWorldLocationException();
        }
        if (!location1.getWorld().equals(location2.getWorld())){
            throw new NotSameWorldLocationException();
        }



                World world=location1.getWorld();

                int xFrom=Math.min(location1.getBlockX(),location2.getBlockX());
                int xTo=Math.max(location1.getBlockX(),location2.getBlockX());
                int yFrom=Math.min(location1.getBlockY(),location2.getBlockY());
                int yTo=Math.max(location1.getBlockY(),location2.getBlockY());

                int zFrom=Math.min(location1.getBlockZ(),location2.getBlockZ());
                int zTo=Math.max(location1.getBlockZ(),location2.getBlockZ());


                List<Location> placed=new ArrayList<>();

                    for (int i = xFrom; i <= xTo; i++) {

                        Location[] locations = new Location[4];
                        locations[0] = new Location(world, i, yFrom, zFrom);
                        locations[1] = new Location(world, i, yTo, zFrom);
                        locations[2] = new Location(world, i, yFrom, zTo);
                        locations[3] = new Location(world, i, yTo, zTo);

                        for (int ii = 0; ii < 4; ii++) {
                            if (placed.contains(locations[ii])){
                                continue;
                            }else {
                                placed.add(locations[ii]);
                            }


                            if (VersionUtil.getMinecraftBigVersion() == 1 && VersionUtil.getMinecraftMiddleVersion() < 13) {
                                Block block = locations[ii].getBlock();
                                BlockState blockState = block.getState();

                                Material oldType = blockState.getType();
                                byte oldRawData = blockState.getRawData();
                                MaterialData oldMaterialData = blockState.getData();

                                blockState.setType(Material.BEDROCK);
                                blockState.setRawData(new MaterialData(Material.BEDROCK).getData());
                                blockState.setData(new MaterialData(Material.BEDROCK));
                                blockState.update(true, false);

                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        blockState.setType(oldType);
                                        blockState.setRawData(oldRawData);
                                        blockState.setData(oldMaterialData);
                                        blockState.update(true, false);

                                    }
                                }.runTaskLater(EasyPlugin.instance, 100);


                            } else {
                                Block block = locations[ii].getBlock();
                                BlockData oldBlockData = block.getBlockData();
                                block.setBlockData(Bukkit.createBlockData(Material.BEDROCK));
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        block.setBlockData(oldBlockData);
                                    }
                                }.runTaskLater(EasyPlugin.instance, 100);
                            }

                        }

                    }


            for (int i = zFrom; i <= zTo; i++) {

                Location[] locations = new Location[4];
                locations[0] = new Location(world, xFrom, yFrom, i);
                locations[1] = new Location(world, xFrom, yTo, i);
                locations[2] = new Location(world, xTo, yFrom, i);
                locations[3] = new Location(world, xTo, yTo, i);

                for (int ii = 0; ii < 4; ii++) {
                    if (placed.contains(locations[ii])){
                        continue;
                    }else {
                        placed.add(locations[ii]);
                    }
                    if (VersionUtil.getMinecraftBigVersion() == 1 && VersionUtil.getMinecraftMiddleVersion() < 13) {
                        Block block = locations[ii].getBlock();
                        BlockState blockState = block.getState();

                        Material oldType = blockState.getType();
                        byte oldRawData = blockState.getRawData();
                        MaterialData oldMaterialData = blockState.getData();

                        blockState.setType(Material.BEDROCK);
                        blockState.setRawData(new MaterialData(Material.BEDROCK).getData());
                        blockState.setData(new MaterialData(Material.BEDROCK));
                        blockState.update(true, false);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                blockState.setType(oldType);
                                blockState.setRawData(oldRawData);
                                blockState.setData(oldMaterialData);
                                blockState.update(true, false);

                            }
                        }.runTaskLater(EasyPlugin.instance, 100);


                    } else {
                        Block block = locations[ii].getBlock();
                        BlockData oldBlockData = block.getBlockData();
                        block.setBlockData(Bukkit.createBlockData(Material.BEDROCK));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                block.setBlockData(oldBlockData);
                            }
                        }.runTaskLater(EasyPlugin.instance, 100);
                    }

                }
            }


            for (int i = yFrom; i <= yTo; i++) {

                Location[] locations = new Location[4];
                locations[0] = new Location(world, xFrom, i, zFrom);
                locations[1] = new Location(world, xFrom, i, zTo);
                locations[2] = new Location(world, xTo, i, zFrom);
                locations[3] = new Location(world, xTo, i, zTo);

                for (int ii = 0; ii < 4; ii++) {
                    if (placed.contains(locations[ii])){
                        continue;
                    }else {
                        placed.add(locations[ii]);
                    }
                    if (VersionUtil.getMinecraftBigVersion() == 1 && VersionUtil.getMinecraftMiddleVersion() < 13) {
                        Block block = locations[ii].getBlock();
                        BlockState blockState = block.getState();

                        Material oldType = blockState.getType();
                        byte oldRawData = blockState.getRawData();
                        MaterialData oldMaterialData = blockState.getData();

                        blockState.setType(Material.BEDROCK);
                        blockState.setRawData(new MaterialData(Material.BEDROCK).getData());
                        blockState.setData(new MaterialData(Material.BEDROCK));
                        blockState.update(true, false);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                blockState.setType(oldType);
                                blockState.setRawData(oldRawData);
                                blockState.setData(oldMaterialData);
                                blockState.update(true, false);

                            }
                        }.runTaskLater(EasyPlugin.instance, 100);


                    } else {
                        Block block = locations[ii].getBlock();
                        BlockData oldBlockData = block.getBlockData();
                        block.setBlockData(Bukkit.createBlockData(Material.BEDROCK));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                block.setBlockData(oldBlockData);
                            }
                        }.runTaskLater(EasyPlugin.instance, 100);
                    }

                }
            }







    }


}

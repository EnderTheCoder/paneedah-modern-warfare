// Date: 3/25/2016 5:34:35 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX






package com.paneedah.mw.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class Laser extends ModelBase
{
  //fields
    ModelRenderer laser1;
    ModelRenderer laser2;
    ModelRenderer laser3;
    ModelRenderer laser4;
    ModelRenderer laser5;
    ModelRenderer laser6;
    ModelRenderer laser7;
    ModelRenderer laser8;
    ModelRenderer laser9;
    ModelRenderer laser11;
  
  public Laser()
  {
    textureWidth = 512;
    textureHeight = 256;
    
      laser1 = new ModelRenderer(this, 0, 0);
      laser1.addBox(0F, 0F, 0F, 3, 3, 10);
      laser1.setRotationPoint(2F, 0F, 0F);
      laser1.setTextureSize(64, 32);
      laser1.mirror = true;
      setRotation(laser1, 0F, 0F, 0F);
      laser2 = new ModelRenderer(this, 0, 0);
      laser2.addBox(0F, 0F, 0F, 2, 1, 10);
      laser2.setRotationPoint(2.5F, -0.3F, 0F);
      laser2.setTextureSize(64, 32);
      laser2.mirror = true;
      setRotation(laser2, 0F, 0F, 0F);
      laser3 = new ModelRenderer(this, 0, 0);
      laser3.addBox(0F, 0F, 0F, 1, 2, 10);
      laser3.setRotationPoint(4.3F, 0.5F, 0F);
      laser3.setTextureSize(64, 32);
      laser3.mirror = true;
      setRotation(laser3, 0F, 0F, 0F);
      laser4 = new ModelRenderer(this, 0, 0);
      laser4.addBox(0F, 0F, 0F, 2, 1, 10);
      laser4.setRotationPoint(2.5F, 2.3F, 0F);
      laser4.setTextureSize(64, 32);
      laser4.mirror = true;
      setRotation(laser4, 0F, 0F, 0F);
      laser5 = new ModelRenderer(this, 0, 0);
      laser5.addBox(0F, 0F, 0F, 1, 2, 4);
      laser5.setRotationPoint(5.3F, 0.5F, 4F);
      laser5.setTextureSize(64, 32);
      laser5.mirror = true;
      setRotation(laser5, 0F, 0F, 0F);
      laser6 = new ModelRenderer(this, 0, 0);
      laser6.addBox(0F, 0F, 0F, 2, 1, 4);
      laser6.setRotationPoint(2.5F, -1.3F, 4F);
      laser6.setTextureSize(64, 32);
      laser6.mirror = true;
      setRotation(laser6, 0F, 0F, 0F);
      laser7 = new ModelRenderer(this, 0, 0);
      laser7.addBox(0F, 0F, 0F, 2, 1, 4);
      laser7.setRotationPoint(2.5F, 3.3F, 4F);
      laser7.setTextureSize(64, 32);
      laser7.mirror = true;
      setRotation(laser7, 0F, 0F, 0F);
      laser8 = new ModelRenderer(this, 0, 0);
      laser8.addBox(0F, 0F, 0F, 1, 2, 4);
      laser8.setRotationPoint(5.8F, 0.5F, 4F);
      laser8.setTextureSize(64, 32);
      laser8.mirror = true;
      setRotation(laser8, 0F, 0F, 2.379431F);
      laser9 = new ModelRenderer(this, 0, 0);
      laser9.addBox(0F, 0F, 0F, 2, 1, 4);
      laser9.setRotationPoint(5.8F, 2.5F, 4F);
      laser9.setTextureSize(64, 32);
      laser9.mirror = true;
      setRotation(laser9, 0F, 0F, 2.379431F);
      laser11 = new ModelRenderer(this, 0, 0);
      laser11.addBox(0F, 0F, 0F, 2, 4, 4);
      laser11.setRotationPoint(0F, -0.5F, 4F);
      laser11.setTextureSize(64, 32);
      laser11.mirror = true;
      setRotation(laser11, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    laser1.render(f5);
    laser2.render(f5);
    laser3.render(f5);
    laser4.render(f5);
    laser5.render(f5);
    laser6.render(f5);
    laser7.render(f5);
    laser8.render(f5);
    laser9.render(f5);
    laser11.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }

}

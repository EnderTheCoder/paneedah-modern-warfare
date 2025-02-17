// Date: 9/30/2017 12:27:45 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX






package com.paneedah.mw.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class makarovfrontsight extends ModelBase
{
  //fields
    ModelRenderer sight2;
    ModelRenderer sight3;
    ModelRenderer sight4;
    ModelRenderer sight5;
  
  public makarovfrontsight()
  {
    textureWidth = 128;
    textureHeight = 64;
    
      sight2 = new ModelRenderer(this, 0, 0);
      sight2.addBox(0F, 0F, 0F, 3, 5, 4);
      sight2.setRotationPoint(0F, -2F, -3F);
      sight2.setTextureSize(64, 32);
      sight2.mirror = true;
      setRotation(sight2, 0F, 0F, 0F);
      sight3 = new ModelRenderer(this, 0, 0);
      sight3.addBox(0F, 0F, 0F, 3, 5, 4);
      sight3.setRotationPoint(0F, -2F, -3F);
      sight3.setTextureSize(64, 32);
      sight3.mirror = true;
      setRotation(sight3, -1.264073F, 0F, 0F);
      sight4 = new ModelRenderer(this, 0, 0);
      sight4.addBox(0F, 0F, 0F, 3, 3, 3);
      sight4.setRotationPoint(0F, -0.4F, -7.75F);
      sight4.setTextureSize(64, 32);
      sight4.mirror = true;
      setRotation(sight4, -0.669215F, 0F, 0F);
      sight5 = new ModelRenderer(this, 0, 0);
      sight5.addBox(0F, 0F, 0F, 3, 1, 7);
      sight5.setRotationPoint(0F, 2F, -9.6F);
      sight5.setTextureSize(64, 32);
      sight5.mirror = true;
      setRotation(sight5, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    sight2.render(f5);
    sight3.render(f5);
    sight4.render(f5);
    sight5.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }

}

package slimeknights.mantle.client.screen.book.element;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import slimeknights.mantle.client.book.action.StringActionProcessor;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ItemElement extends SizedBookElement {

  public static final int ITEM_SIZE_HARDCODED = 16;
  public static final long ITEM_SWITCH_TIME = 3000000000L; // 3 seconds

  public NonNullList<ItemStack> itemCycle;
  public float scale;
  @Nullable
  public String action;
  public List<ITextComponent> tooltip;

  public long lastTime;
  public int currentItem = 0;

  public ItemElement(int x, int y, float scale, Item item) {
    this(x, y, scale, new ItemStack(item));
  }

  public ItemElement(int x, int y, float scale, Block item) {
    this(x, y, scale, new ItemStack(item));
  }

  public ItemElement(int x, int y, float scale, ItemStack item) {
    this(x, y, scale, new ItemStack[] { item });
  }

  public ItemElement(int x, int y, float scale, Collection<ItemStack> itemCycle) {
    this(x, y, scale, itemCycle.toArray(new ItemStack[0]));
  }

  public ItemElement(int x, int y, float scale, Collection<ItemStack> itemCycle, String action) {
    this(x, y, scale, itemCycle.toArray(new ItemStack[0]), action);
  }

  public ItemElement(int x, int y, float scale, ItemStack... itemCycle) {
    this(x, y, scale, itemCycle, null);
  }

  public ItemElement(int x, int y, float scale, ItemStack[] itemCycle, @Nullable String action) {
    super(x, y, MathHelper.floor(ITEM_SIZE_HARDCODED * scale), MathHelper.floor(ITEM_SIZE_HARDCODED * scale));

    lastTime = Util.nanoTime();

    NonNullList<ItemStack> nonNullStacks = NonNullList.withSize(itemCycle.length, ItemStack.EMPTY);
    for (int i = 0; i < itemCycle.length; i++) {
      if (!itemCycle[i].isEmpty()) {
        nonNullStacks.set(i, itemCycle[i].copy());
      }
    }

    this.itemCycle = nonNullStacks;
    this.scale = scale;
    this.action = action;
  }

  @Override
  public void draw(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, FontRenderer fontRenderer) {
    long nano = Util.nanoTime();

    if(nano > lastTime + ITEM_SWITCH_TIME) {
      this.lastTime = nano;
      this.currentItem++;

      if (this.currentItem >= this.itemCycle.size()) {
        this.currentItem = 0;
      }
    }

    if (this.currentItem < this.itemCycle.size()) {
      RenderSystem.pushMatrix();
      RenderSystem.translatef(this.x, this.y, 0);
      RenderSystem.scalef(this.scale, this.scale, 1.0F);
      ItemStack stack = this.itemCycle.get(this.currentItem);
      this.mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, 0, 0);
      FontRenderer font = stack.getItem().getFontRenderer(stack);
      if (font == null) font = mc.fontRenderer;
      this.mc.getItemRenderer().renderItemOverlayIntoGUI(font, stack, 0, 0, null);

      RenderSystem.popMatrix();
      RenderHelper.disableStandardItemLighting();
    }
  }

  @Override
  public void drawOverlay(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, FontRenderer fontRenderer) {
    if (this.isHovered(mouseX, mouseY) && this.currentItem < this.itemCycle.size()) {
      if (this.tooltip != null) {
        this.drawHoveringText(matrixStack, this.tooltip, mouseX, mouseY, fontRenderer);
      }
      else {
        this.renderToolTip(matrixStack, fontRenderer, this.itemCycle.get(this.currentItem), mouseX, mouseY);
      }
    }
  }

  @Override
  public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
    if (mouseButton == 0 && this.isHovered(mouseX, mouseY) && this.currentItem < this.itemCycle.size()) {
      if (this.action != null) {
        StringActionProcessor.process(this.action, this.parent);
      }
      else {
        this.parent.itemClicked(this.itemCycle.get(this.currentItem));
      }
    }
  }
}

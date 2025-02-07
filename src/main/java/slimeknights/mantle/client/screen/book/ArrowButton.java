package slimeknights.mantle.client.screen.book;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import slimeknights.mantle.client.book.data.BookData;

import javax.annotation.Nullable;

import static slimeknights.mantle.client.screen.book.Textures.TEX_BOOK;

@OnlyIn(Dist.CLIENT)
public class ArrowButton extends Button {

  public static final int WIDTH = 18;
  public static final int HEIGHT = 10;

  // Appearance
  @Nullable
  private final BookData bookData;
  public ArrowType arrowType;
  public int color;
  public int hoverColor;

  public ArrowButton(@Nullable BookData bookData, int x, int y, ArrowType arrowType, int color, int hoverColor, IPressable iPressable) {
    super(x, y, arrowType.w, arrowType.h, StringTextComponent.EMPTY, iPressable);

    this.arrowType = arrowType;
    this.color = color;
    this.hoverColor = hoverColor;
    this.bookData = bookData;
  }

  public ArrowButton(int x, int y, ArrowType arrowType, int color, int hoverColor, IPressable iPressable) {
    this(null, x, y, arrowType, color, hoverColor, iPressable);
  }

  public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, @Nullable BookData bookData) {
    Minecraft minecraft = Minecraft.getInstance();
    if (bookData != null) {
      minecraft.getTextureManager().bindTexture(bookData.appearance.getBookTexture());
    } else {
      minecraft.getTextureManager().bindTexture(TEX_BOOK);
    }

    this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

    int color = this.isHovered ? this.hoverColor : this.color;

    float r = ((color >> 16) & 0xff) / 255.F;
    float g = ((color >> 8) & 0xff) / 255.F;
    float b = (color & 0xff) / 255.F;

    RenderSystem.color3f(r, g, b);
    blit(matrixStack, this.x, this.y, this.width, this.height, this.arrowType.x, this.arrowType.y, this.width, this.height, 512, 512);
    this.renderBg(matrixStack, minecraft, mouseX, mouseY);
  }

  @Override
  public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    renderButton(matrixStack, mouseX, mouseY, partialTicks, bookData);
  }

  public enum ArrowType {
    NEXT(412, 0),
    PREV(412, 10),
    RIGHT(412, 20),
    LEFT(412, 30),
    BACK_UP(412, 40, 18, 18),
    UP(412, 58, 10, 18),
    DOWN(412 + 10, 58, 10, 18),
    REFRESH(412, 76, 18, 18);

    public final int x, y, w, h;

    ArrowType(int x, int y) {
      this(x, y, WIDTH, HEIGHT);
    }

    ArrowType(int x, int y, int w, int h) {
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
    }
  }
}

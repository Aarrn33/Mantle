package slimeknights.mantle.client.book.data;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import slimeknights.mantle.client.screen.book.Textures;
import slimeknights.mantle.util.LogicHelper;

import javax.annotation.Nullable;

/** This class represents the JSON structure of book appearance */
@OnlyIn(Dist.CLIENT)
@SuppressWarnings("unused")
public class AppearanceData implements IDataItem {
  // cover
  @Nullable
  private ResourceLocation coverTexture;
  /** Title on the cover of the book */
  public String title = "";
  /** Smaller subtitle, typically author information */
  public String subtitle = "";
  /** Color to tint the cover background */
  public int coverColor = 0x8B4631;

  // general book
  @Nullable
  private ResourceLocation bookTexture;
  /** Color to tint navigation arrows */
  public int arrowColor = 0xFFFFD3;
  /** Color to tint hovered navigation arrows */
  public int arrowColorHover = 0xFF541C;
  /** Color used when hovering over a selectable element */
  public int hoverColor = 0x77EE541C;
  /** If true, page numbers are drawn below each page */
  public boolean drawPageNumbers = true;
  /** If true, draws the text below elements in selection lists such as indexes */
  public boolean drawSectionListText = false;
  /** If true, draws the index with 4 columns. False draws with 3 */
  public boolean drawFourColumnIndex = false;

  /** If true, titles on pages will be centered */
  public boolean centerPageTitles = false;
  /** If true, page titles will be rendered slightly larger */
  public boolean largePageTitles = false;

  // specific sections
  /** Color of slots in various recipes */
  public int slotColor = 0xFF844C;
  /** Color to tint locked content */
  public int lockedSectionColor = 0x000000;
  /** Color of the button for structure UIs */
  public int structureButtonColor = 0xe3E3BC;
  /** Color of the button for structure UIs when hovered */
  public int structureButtonColorHovered = 0x76D1E8;
  /** Color of the button for structure UIs when animation is toggled */
  public int structureButtonColorToggled = 0x67C768;

  /** Currently unused, purpose uncertain */
  public float scale = 0.5F;

  /** Gets the book cover texture */
  public ResourceLocation getCoverTexture() {
    return LogicHelper.defaultIfNull(coverTexture, Textures.TEX_BOOKFRONT);
  }

  /** Gets texture for book pages and elements */
  public ResourceLocation getBookTexture() {
    return LogicHelper.defaultIfNull(bookTexture, Textures.TEX_BOOK);
  }

  @Override
  public void load() {}
}

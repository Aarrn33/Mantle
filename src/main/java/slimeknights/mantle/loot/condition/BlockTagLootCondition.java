package slimeknights.mantle.loot.condition;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import lombok.RequiredArgsConstructor;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameter;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import slimeknights.mantle.loot.MantleLoot;
import slimeknights.mantle.util.JsonHelper;

import java.util.Set;

/** Variant of {@link net.minecraft.loot.conditions.BlockStateProperty} that allows using a tag for block type instead of a block */
@RequiredArgsConstructor
public class BlockTagLootCondition implements ILootCondition {
  public static final Serializer SERIALIZER = new Serializer();

  private final ITag<Block> tag;
  private final StatePropertiesPredicate properties;

  public BlockTagLootCondition(ITag<Block> tag) {
    this(tag, StatePropertiesPredicate.EMPTY);
  }

  public BlockTagLootCondition(ITag<Block> tag, StatePropertiesPredicate.Builder builder) {
    this(tag, builder.build());
  }

  @Override
  public boolean test(LootContext context) {
    BlockState state = context.get(LootParameters.BLOCK_STATE);
    return state != null && state.isIn(tag) && this.properties.matches(state);
  }

  @Override
  public Set<LootParameter<?>> getRequiredParameters() {
    return ImmutableSet.of(LootParameters.BLOCK_STATE);
  }

  @Override
  public LootConditionType func_230419_b_() {
    return MantleLoot.BLOCK_TAG_CONDITION;
  }

  private static class Serializer implements ILootSerializer<BlockTagLootCondition> {
    @Override
    public void serialize(JsonObject json, BlockTagLootCondition loot, JsonSerializationContext context) {
      json.addProperty("tag", TagCollectionManager.getManager().getBlockTags().getValidatedIdFromTag(loot.tag).toString());
      if (loot.properties != StatePropertiesPredicate.EMPTY) {
        json.add("properties", loot.properties.toJsonElement());
      }
    }

    @Override
    public BlockTagLootCondition deserialize(JsonObject json, JsonDeserializationContext context) {
      ResourceLocation id = JsonHelper.getResourceLocation(json, "tag");
      ITag<Block> tag = TagCollectionManager.getManager().getBlockTags().get(id);
      if (tag == null) {
        throw new JsonSyntaxException("Unknown block tag '" + id + "'");
      }
      StatePropertiesPredicate predicate = StatePropertiesPredicate.EMPTY;
      if (json.has("properties")) {
        predicate = StatePropertiesPredicate.deserializeProperties(json.get("properties"));
      }
      return new BlockTagLootCondition(tag, predicate);
    }
  }
}

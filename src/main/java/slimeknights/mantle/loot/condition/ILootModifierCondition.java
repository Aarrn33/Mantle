package slimeknights.mantle.loot.condition;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import slimeknights.mantle.util.GenericRegisteredSerializer;
import slimeknights.mantle.util.GenericRegisteredSerializer.IJsonSerializable;

import java.util.List;

/** Condition for the global loot modifier add entry */
public interface ILootModifierCondition extends IJsonSerializable {
  /** Serializer to register conditions with */
  GenericRegisteredSerializer<ILootModifierCondition> MODIFIER_CONDITIONS = new GenericRegisteredSerializer<>();

  /** Checks if this condition passes */
  boolean test(List<ItemStack> generatedLoot, LootContext context);

  /** Creates an inverted instance of this condition */
  default ILootModifierCondition inverted() {
    return new InvertedModifierLootCondition(this);
  }
}

package org.xiaoxigua.xchat.common.unitll.component

import io.netty.buffer.ByteBuf
import net.kyori.adventure.nbt.BinaryTag
import org.xiaoxigua.xchat.common.unitll.Codecs

enum class ComponentType {
    CustomData,
    MaxStackSize,
    MaxDamage,
    Damage,
    Unbreakable,
    CustomName,
    ItemName,
    Lore,
    Rarity,
    Enchantments,
    CanPlaceOn,
    CanBreak,
    AttributeModifiers,
    CustomModelData,
    HideAdditionalTooltip,
    HideTooltip,
    RepairCost,
    CreativeSlotLock,
    EnchantmentGlintOverride,
    IntangibleProjectile,
    Food,
    FireResistant,
    Tool,
    StoredEnchantments,
    DyedColor,
    MapColor,
    MapId,
    MapDecorations,
    MapPostProcessing,
    ChargedProjectiles,
    BundleContents,
    PotionContents,
    SuspiciousStewEffects,
    WritableBookContent,
    WrittenBookContent,
    Trim,
    DebugStickState,
    EntityData,
    BucketEntityData,
    BlockEntityData,
    Instrument,
    OminousBottleAmplifier,
    JukeboxPlayable,
    Recipes,
    LodestoneTracker,
    FireworkExplosion,
    Fireworks,
    Profile,
    NoteBlockSound,
    BannerPatterns,
    BaseColor,
    PotDecorations,
    Container,
    BlockState,
    Bees,
    Lock,
    ContainerLoot;

    companion object {
        fun getComponentType(i: Int) = entries[i]
    }

    fun getTypeString(): String {
        val humps = "(?<=.)(?=\\p{Upper})".toRegex()
        val name = this.name.replace(humps, "_").lowercase()

        return name
    }

    fun getIndex() = entries.indexOf(this)

    class ComponentCodec(private val buf: ByteBuf) : Codecs<ComponentType>() {
        val componentType = decode(buf)

        override fun encode(buf: ByteBuf, value: ComponentType) {
            buf.writeByte(value.getIndex())
        }

        override fun decode(buf: ByteBuf) = getComponentType(ByteCodec.decode(buf).toInt())

        fun value(): BinaryTag? {
            return when (componentType) {
                MaxStackSize, Rarity -> IntBinaryTagCodec.decode(buf)
                CustomName, ItemName -> StringBinaryTagCodec.decode(buf)
                else -> {
                    println("$componentType")
                    null
                }
            }
        }
    }
}
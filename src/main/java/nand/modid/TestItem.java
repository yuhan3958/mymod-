package nand.modid;

import net.minecraft.block.BlockState;
import net.minecraft.command.EntitySelectorReader;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class TestItem extends Item {

    public TestItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            user.addVelocity(0, 1.5, 0); // 위로 1.5
            user.velocityModified = true;
            user.sendMessage(Text.literal("테스트 아이템 사용됨!"), false);
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        if (!target.getWorld().isClient) {
            World world = target.getWorld();

            LightningEntity lightning = new LightningEntity(
                    EntityType.LIGHTNING_BOLT,
                    world
            );

            lightning.setPosition(
                    target.getX(),
                    target.getY(),
                    target.getZ()
            );

            world.spawnEntity(lightning);
        }

        attacker.heal(5.0f);

        return true;
    }


    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient && miner instanceof PlayerEntity player) {

            TntEntity tnt = new TntEntity(
                    world,
                    pos.getX() + 0.5,
                    pos.getY() + 1,
                    pos.getZ() + 0.5,
                    player
            );

            tnt.setFuse(40); // 40틱 = 2초
            world.spawnEntity(tnt);

            player.sendMessage(Text.literal("💣 TNT 소환!"), false);
        }
        return true;
    }
}

package com.vicmatskiv.mw;

import static com.vicmatskiv.weaponlib.compatibility.CompatibilityProvider.compatibility;

import java.io.File;

import com.vicmatskiv.weaponlib.ModContext;
import com.vicmatskiv.weaponlib.compatibility.CompatibleFmlInitializationEvent;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

@Mod(modid = ModernWarfareMod.MODID, version = ModernWarfareMod.VERSION)
public class ModernWarfareMod {

	private static final String CONFIG_PROPERTY_CATEGORY_GENERAL = "general";
	private static final String CONFIG_PROPERTY_ORE_GENERATION_ENABLED = "Ore generation enabled";
	private static final String MODERN_WARFARE_CONFIG_FILE_NAME = "ModernWarfare.cfg";
	public static final String MODID = "mw";
	public static final String VERSION = "1.8";

	@SidedProxy(serverSide = "com.vicmatskiv.weaponlib.CommonModContext", clientSide = "com.vicmatskiv.weaponlib.ClientModContext")
	public static ModContext MOD_CONTEXT;

	public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

	public static CreativeTabs gunsTab = new GunsTab(
			CreativeTabs.getNextID(), "guns_tab");

	public static CreativeTabs AssaultRiflesTab = new AssaultRiflesTab(
            CreativeTabs.getNextID(), "AssaultRifles_tab");

	public static CreativeTabs PistolsTab = new PistolsTab(
            CreativeTabs.getNextID(), "Pistols_tab");

	public static CreativeTabs SMGTab = new SMGTab(
            CreativeTabs.getNextID(), "SMG_tab");

	public static CreativeTabs ShotgunsTab = new ShotgunsTab(
            CreativeTabs.getNextID(), "ShotgunsTab");

	public static CreativeTabs SnipersTab = new SnipersTab(
            CreativeTabs.getNextID(), "SnipersTab");

	public static CreativeTabs AR2Tab = new AR2Tab(
            CreativeTabs.getNextID(), "AR2Tab");

	public static CreativeTabs FunGunsTab = new FunGunsTab(
			CreativeTabs.getNextID(), "FunGuns_tab");

	@SidedProxy(serverSide = "com.vicmatskiv.mw.CommonProxy",
			clientSide = "com.vicmatskiv.mw.ClientProxy")
	public static CommonProxy proxy;

	public static boolean oreGenerationEnabled = true;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		configure(event.getSuggestedConfigurationFile());
		proxy.init(this, new CompatibleFmlInitializationEvent(event));
	}

	private void configure(File suggestedConfig) {
		File parentDirectory = suggestedConfig.getParentFile();
		File configFile;
		if(parentDirectory != null) {
			configFile = new File(parentDirectory, MODERN_WARFARE_CONFIG_FILE_NAME);
		} else {
			configFile = new File(MODERN_WARFARE_CONFIG_FILE_NAME);
		}
		Configuration config = new Configuration(configFile);
		config.load();
		Property oreGenerationEnabled = config.get(CONFIG_PROPERTY_CATEGORY_GENERAL, CONFIG_PROPERTY_ORE_GENERATION_ENABLED, true);
		ModernWarfareMod.oreGenerationEnabled = oreGenerationEnabled.getBoolean();
		config.save();
	}

	// ItemRecipes
	@EventHandler
	public void init(FMLInitializationEvent event) {

		GameRegistry.addSmelting(CommonProxy.TitaniumOre, new ItemStack(CommonProxy.TitaniumIngot), 5f);
		GameRegistry.addSmelting(CommonProxy.CopperOre, new ItemStack(CommonProxy.CopperIngot), 5f);
		GameRegistry.addSmelting(CommonProxy.LeadOre, new ItemStack(CommonProxy.LeadIngot), 5f);
		GameRegistry.addSmelting(CommonProxy.TinOre, new ItemStack(CommonProxy.TinIngot), 5f);
		GameRegistry.addSmelting(CommonProxy.BauxiteOre, new ItemStack(CommonProxy.AluminumIngot), 5f);
		GameRegistry.addSmelting(CommonProxy.SiliconOre, new ItemStack(CommonProxy.Silicon), 5f);
		GameRegistry.addSmelting(CommonProxy.TantalumOre, new ItemStack(CommonProxy.TantalumIngot), 5f);
		GameRegistry.addSmelting(CommonProxy.SulfurDust, new ItemStack(Items.gunpowder), 5f);
		GameRegistry.addSmelting(CommonProxy.SteelDust, new ItemStack(CommonProxy.SteelIngot), 5f);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.Marinehelmet),
				"AAA",
				"AGA",
				"X X",
				'A', CommonProxy.TanCloth,
				'X', "ingotTitanium");

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.Marinechest),
				"A A",
				"AAA",
				"XXX",
				'A', CommonProxy.TanCloth,
				'X', "ingotTitanium");

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.Marineboots),
				"A A",
				"A A",
				"X X",
				'A', CommonProxy.TanCloth,
				'X', "ingotTitanium");

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.Spetznazhelmet),
				"AAA",
				"AGA",
				"X X",
				'A', CommonProxy.GreenCloth,
				'X', "ingotTitanium");

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.Spetznazchest),
				"A A",
				"AAA",
				"XXX",
				'A', CommonProxy.GreenCloth,
				'X', "ingotTitanium");

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.Spetznazboots),
				"A A",
				"A A",
				"X X",
				'A', CommonProxy.GreenCloth,
				'X', "ingotTitanium");

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Cloth, 3),
				"XAX",
				"AXA",
				"XAX",
				'X', Items.string,
				'A', Blocks.wool
		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.TanCloth, 3),
				"XAX",
				"AXA",
				"XAX",
				'X', Blocks.sandstone,
				'A', CommonProxy.Cloth
		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.GreenCloth, 3),
				"XAX",
				"AXA",
				"XAX",
				'X', Blocks.leaves,
				'A', CommonProxy.Cloth
		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Electronics),
				"TAI",
				"ACA",
				"RAD",
				'I', CommonProxy.Inductor,
				'T', CommonProxy.Transistor,
				'R', CommonProxy.Resistor,
				'D', CommonProxy.Diode,
				'C', CommonProxy.Capacitor,
				'A', CommonProxy.CopperWiring
		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.CGrip),
				"AAA",
				"XBX",
				"  A",
				'A', CommonProxy.SteelPlate,
				'X', CommonProxy. SteelIngot,
				'B', Blocks.stone_button
		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.OpticGlass),
				"XAX",
				"AEA",
				"XAX",
				'X', "ingotTitanium",
				'E', CommonProxy.CopperWiring,
				'A', Blocks.glass_pane

		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.MiniSteelPlate, 2),
				"XX",
				'X', CommonProxy.SteelIngot
		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.SteelPlate, 2),
				"XXX",
				"XXX",
				'X', CommonProxy.SteelIngot
		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.BigSteelPlate),
                "XAX",
                'X', CommonProxy.SteelPlate,
                'A', CommonProxy.MiniSteelPlate
        );

		GameRegistry.addRecipe(new ItemStack(CommonProxy.MetalComponents),
                "XA",
                "AX",
                'X', CommonProxy.SteelPlate,
                'A', CommonProxy.SteelIngot
        );

		GameRegistry.addRecipe(new ItemStack(CommonProxy.M9SD),
                "AX",
                'X', CommonProxy.M9,
                'A', CommonProxy.Silencer9mm
        );

		GameRegistry.addRecipe(new ItemStack(CommonProxy.SMAWRocket),
                "AXX",
                'X', CommonProxy.SteelPlate,
                'A', Blocks.tnt
        );

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Bullets, 32),
				" R ",
				"XAX",
				"XAX",
				'X', CommonProxy.CopperIngot,
				'R', CommonProxy.LeadIngot,
				'A', Items.gunpowder
		);

//		GameRegistry.addRecipe(new ItemStack(CommonProxy.AK12),
//				"XA ",
//				" EA",
//				" QF",
//				'X', CommonProxy.Barrel,
//				'A', CommonProxy.SteelPlate,
//				'E', CommonProxy.WeaponReceiver,
//				'Q', CommonProxy.CGrip,
//				'F', CommonProxy.WeaponStock
//		);

		/*GameRegistry.addRecipe(new ItemStack(CommonProxy.AK47Mag),
				" XX",
				"XBX",
				"ABX",
				'X', CommonProxy.MiniSteelPlate,
				'B', CommonProxy.Bullets
		);*/

//		GameRegistry.addRecipe(new ItemStack(CommonProxy.AK74UMag),
//				" XX",
//				"WBW",
//				"XBW",
//				'X', CommonProxy.MiniSteelPlate,
//				'B', CommonProxy.Bullets,
//				'W', Blocks.planks
//		);

//		GameRegistry.addRecipe(new ItemStack(CommonProxy.MP5Mag),
//				"AXA",
//				"XBA",
//				"XBX",
//				'X', CommonProxy.SteelIngot,
//				'A', CommonProxy.MiniSteelPlate,
//				'B', CommonProxy.Bullets,
//				'W', Blocks.planks
//		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.ACOG),
				"FXA",
				"ORG",
				"AXX",
				'R', CommonProxy.Electronics,
				'A', CommonProxy.MiniSteelPlate,
				'X', CommonProxy.SteelIngot,
				'O', CommonProxy.OpticGlass,
				'G', Blocks.glass_pane,
				'F', CommonProxy.CopperWiring
		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Holo2),
				"  A",
				" RO",
				"AXX",
				'R', CommonProxy.Electronics,
				'A', CommonProxy.MiniSteelPlate,
				'X', CommonProxy.SteelIngot,
				'O', CommonProxy.OpticGlass,
				'G', Blocks.glass_pane
		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Holographic2),
                "  A",
                "XRO",
                "AXX",
                'R', CommonProxy.Electronics,
                'A', CommonProxy.MiniSteelPlate,
                'X', CommonProxy.SteelIngot,
                'O', CommonProxy.OpticGlass,
                'G', Blocks.glass_pane
        );

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Reflex),
				"A  ",
				"ORX",
				"AXX",
				'R', CommonProxy.Electronics,
				'A', CommonProxy.MiniSteelPlate,
				'X', CommonProxy.SteelIngot,
				'O', CommonProxy.OpticGlass,
				'G', Blocks.glass_pane
		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Kobra),
				"X  ",
				"OGX",
				"ARX",
				'R', CommonProxy.Electronics,
				'A', CommonProxy.MiniSteelPlate,
				'X', CommonProxy.SteelIngot,
				'O', CommonProxy.OpticGlass,
				'G', Blocks.glass_pane
		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Scope),
				"ARR",
				"OGO",
				"XXX",
				'R', CommonProxy.Electronics,
				'A', CommonProxy.MiniSteelPlate,
				'X', CommonProxy.SteelIngot,
				'O', CommonProxy.OpticGlass,
				'G', Blocks.glass_pane
		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.HP),
				"ARX",
				"ORO",
				"AXX",
				'R', CommonProxy.Electronics,
				'A', CommonProxy.MiniSteelPlate,
				'X', CommonProxy.SteelIngot,
				'O', CommonProxy.OpticGlass
		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Grip),
				"X A",
				" XX",
				'X', CommonProxy.SteelIngot,
				'A', CommonProxy.MiniSteelPlate
		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Grip2),
				"AXX",
				" X ",
				" X ",
				'X', CommonProxy.SteelIngot,
				'A', CommonProxy.MiniSteelPlate
		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.StubbyGrip),
                "AXA",
                " X ",
                'X', CommonProxy.SteelIngot,
                'A', CommonProxy.MiniSteelPlate
        );

		GameRegistry.addRecipe(new ItemStack(CommonProxy.VGrip),
				"XAX",
				" X ",
				" X ",
				'X', CommonProxy.SteelIngot,
				'A', CommonProxy.MiniSteelPlate
		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Bipod),
				" X ",
				"A A",
				"X X",
				'X', CommonProxy.SteelIngot,
				'A', CommonProxy.MiniSteelPlate
		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Laser),
				" X ",
				"ARE",
				"AXX",
				'X', CommonProxy.SteelIngot,
				'A', CommonProxy.MiniSteelPlate,
				'R', CommonProxy.LaserPointer,
				'E', CommonProxy.Electronics
		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Laser2),
				"AXA",
				"XRE",
				"AXX",
				'X', CommonProxy.SteelIngot,
				'A', CommonProxy.MiniSteelPlate,
				'R', CommonProxy.LaserPointer,
				'E', CommonProxy.Electronics
		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Diamond),
                "A",
                'A', Items.diamond
        );

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Gold),
                "A",
                'A', Items.gold_ingot
        );

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Emerald),
                "A",
                'A', Items.emerald
        );

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Sapphire),
                "A",
                'A', new ItemStack(Items.dye, 4, 4)
        );

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Arctic),
                "AR",
                'A', new ItemStack(Items.dye, 0, 0),
                'R', new ItemStack(Items.dye, 15, 15)
        );

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Amber),
                "A",
                'A', new ItemStack(Items.dye, 14, 14)
        );

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Amethyst),
                "A",
                'A', new ItemStack(Items.dye, 5, 5)
        );

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Desert),
                "A",
                'A', Blocks.sand
        );

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Forest),
                "A",
                'A', Blocks.leaves
        );

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Fade),
                "A",
                'A', CommonProxy.Ruby
        );

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Voltaic),
                "AF",
                'A', new ItemStack(Items.dye, 5, 5),
                'F', new ItemStack(Items.dye, 8, 8)
        );

		GameRegistry.addRecipe(new ItemStack(CommonProxy.LightningStrike),
                "AF",
                'A', new ItemStack(Items.dye, 5, 5),
                'F', new ItemStack(Items.dye, 9, 9)
        );

		GameRegistry.addRecipe(new ItemStack(CommonProxy.ElectricSkin),
                "AF",
                'A', new ItemStack(Items.dye, 0, 0),
                'F', new ItemStack(Items.dye, 12, 12)
        );

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Vulcan),
                "AFR",
                'A', new ItemStack(Items.dye, 0, 0),
                'F', new ItemStack(Items.dye, 12, 12),
                'R', new ItemStack(Items.dye, 15, 15)
        );

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Asiimov),
                "AFR",
                'A', new ItemStack(Items.dye, 0, 0),
                'F', new ItemStack(Items.dye, 14, 14),
                'R', new ItemStack(Items.dye, 15, 15)
        );

		GameRegistry.addRecipe(new ItemStack(CommonProxy.LaserPointer),
				"XXX",
				"AAR",
				"XXX",
				'X', CommonProxy.MiniSteelPlate,
				'A', CommonProxy.Ruby,
				'R', CommonProxy.Capacitor
		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Resistor, 3),
				"A",
				"X",
				"A",
				'A', CommonProxy.SteelIngot,
				'X', Items.coal

		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Transistor, 3),
				"AA",
				"XX",
				"XX",
				'A', CommonProxy.SteelIngot,
				'X', CommonProxy.Silicon

		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Capacitor, 3),
				"XX",
				"AA",
				"RR",
				'X', CommonProxy.AluminumIngot,
				'A', CommonProxy.Plastic,
				'R', CommonProxy.TantalumIngot
		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Inductor, 3),
				" A ",
				"AXA",
				" A ",
				'X', CommonProxy.Plastic,
				'A', CommonProxy.CopperWiring
		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Diode, 3),
				"X",
				"A",
				"X",
				'A', CommonProxy.SteelIngot,
				'X', CommonProxy.Silicon
		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.CopperWiring, 10),
				"AAA",
				'A', CommonProxy.CopperIngot
	);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.Plastic, 3),
				"AX",
				'A', Items.coal,
				'X', Items.water_bucket
		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.Piston),
				"XA ",
				"AXX",
				" XE",
				'A', CommonProxy.SteelPlate,
				'X', "ingotSteel",
				'E', CommonProxy.Electronics
		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.SteelDust, 4),
				" X ",
				"XAX",
				" X ",
				'A', Items.iron_ingot,
				'X', Items.coal
		);

		GameRegistry.addRecipe(new ItemStack(CommonProxy.AluminumPlate, 6),
				"XX",
				'X', CommonProxy.AluminumIngot
		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.Magazine762x39),
				"  X",
				"X X",
				"XX ",
				'X', CommonProxy.SteelIngot

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.PMAG762x39),
				"  R",
				"R X",
				"XR ",
				'X', CommonProxy.SteelIngot,
				'R', CommonProxy.MiniSteelPlate

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.AK12Mag),
				"  R",
				"X R",
				"XR ",
				'X', CommonProxy.SteelIngot,
				'R', CommonProxy.MiniSteelPlate

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.RPK74MMag),
				"  X",
				"X X",
				"RR ",
				'X', CommonProxy.SteelIngot,
				'R', CommonProxy.MiniSteelPlate

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.AK74MMag),
				"  R",
				"R X",
				"RX ",
				'X', CommonProxy.SteelIngot,
				'R', CommonProxy.MiniSteelPlate

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.AK74Mag),
				"  R",
				"X X",
				"RX ",
				'X', Items.iron_ingot,
				'R', CommonProxy.MiniSteelPlate

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.AKS74UMag),
				"  X",
				"X R",
				"XR ",
				'X', Items.iron_ingot,
				'R', CommonProxy.SteelIngot

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.NATOMag1),
				"  X",
				"X X",
				"XX ",
				'X', CommonProxy.AluminumIngot

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.NATOMag2),
				"  X",
				"X R",
				"XR ",
				'X', CommonProxy.AluminumIngot,
				'R', CommonProxy.SteelIngot

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.NATOFamasMag),
				"X X",
				"  X",
				" XX",
				'X', CommonProxy.AluminumIngot,
				'R', CommonProxy.SteelIngot

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.NATOG36Mag),
				"  X",
				"X X",
				"RR ",
				'X', CommonProxy.SteelIngot,
				'R', CommonProxy.MiniSteelPlate

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.DragunovMag),
				"X X",
				" RR",
				'X', CommonProxy.SteelIngot,
				'R', CommonProxy.MiniSteelPlate

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.FALMag),
				"X R",
				"  R",
				" RR",
				'X', CommonProxy.SteelIngot,
				'R', CommonProxy.MiniSteelPlate

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.M110Mag),
				"X R",
				"  R",
				" XR",
				'X', Items.iron_ingot,
				'R', CommonProxy.AluminumIngot

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.M14DMRMag),
				" R",
				" R",
				"XX",
				'X', CommonProxy.SteelIngot,
				'R', CommonProxy.MiniSteelPlate

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.Glock21Mag),
				"X",
				"X",
				'X', CommonProxy.SteelIngot,
				'R', CommonProxy.MiniSteelPlate

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.Glock18Mag),
				"R",
				"X",
				'X', CommonProxy.SteelIngot,
				'R', CommonProxy.MiniSteelPlate

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.G18Mag),
                "R",
                "X",
                "X",
                'X', CommonProxy.SteelIngot,
                'R', CommonProxy.MiniSteelPlate

        );

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.NATODrum100),
				" X ",
				"R R",
				"R R",
				'X', CommonProxy.SteelIngot,
				'R', CommonProxy.MiniSteelPlate

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.M9BerettaMag),
				"X",
				"R",
				'X', CommonProxy.SteelIngot,
				'R', CommonProxy.MiniSteelPlate

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.MP40Mag),
				"X",
				"X",
				"X",
				'X', CommonProxy.SteelIngot,
				'R', CommonProxy.MiniSteelPlate

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.MP5KMag),
				" R",
				" X",
				"X ",
				'X', CommonProxy.SteelIngot,
				'R', CommonProxy.MiniSteelPlate

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.DeagleMag),
				"X",
				"X",
				'X', CommonProxy.AluminumIngot

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.Deagle50Mag),
                "X",
                "R",
                'X', CommonProxy.AluminumIngot,
                'R', CommonProxy.MiniSteelPlate

        );

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.AS50Mag),
				"RXR",
				"  X",
				'R', CommonProxy.AluminumIngot,
				'X', CommonProxy.MiniSteelPlate

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.FNP90Mag),
				"XXX",
				'X', CommonProxy.CopperIngot

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.M107BMag),
				"RAR",
				"  A",
				'R', CommonProxy.MiniSteelPlate,
				'A', CommonProxy.SteelIngot

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.HKMP7Mag),
				"RA",
				" A",
				"A ",
				'A', CommonProxy.SteelIngot,
				'R', CommonProxy.MiniSteelPlate

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.M1CarbineMag),
				" A",
				" R",
				"A ",
				'A', CommonProxy.SteelIngot,
				'R', CommonProxy.MiniSteelPlate

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.M240Mag),
				"  A",
				"A A",
				"AAA",
				'A', Items.iron_ingot

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.M249Mag),
				"  R",
				"A A",
				"AAA",
				'A', Items.iron_ingot,
				'R', CommonProxy.MiniSteelPlate

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.MXMag),
				" R",
				" A",
				"AR",
				'A', Items.iron_ingot,
				'R', CommonProxy.SteelIngot

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.M41AMag),
				" A",
				" R",
				"AA",
				'A', CommonProxy.MiniSteelPlate,
				'A', CommonProxy.SteelIngot

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.L115Mag),
				"AA",
				" A",
				'A', CommonProxy.MiniSteelPlate

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.ColtM1911Mag),
				"R",
				"A",
				'A', CommonProxy.MiniSteelPlate,
				'R', CommonProxy.SteelIngot

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.HK33Mag),
				"  R",
				"R A",
				"AR ",
				'R', CommonProxy.MiniSteelPlate,
				'A', CommonProxy.SteelIngot

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.Mag10mm),
				" A",
				" R",
				'A', CommonProxy.SteelIngot,
				'R', CommonProxy.MiniSteelPlate

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.Magazine9mm),
				" A",
				" A",
				"RR",
				'A', CommonProxy.SteelIngot,
				'R', CommonProxy.MiniSteelPlate

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.ScarHMag),
				"RR",
				" A",
				" A",
				'A', CommonProxy.SteelIngot,
				'R', CommonProxy.MiniSteelPlate

		);

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.AK101Mag),
		        "  A",
		        "A R",
		        "AR ",
		        'A', CommonProxy.SteelIngot,
		        'R', CommonProxy.MiniSteelPlate

		        );

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.VectorMag),
		        " A",
		        " A",
		        "AA",
		        'A', CommonProxy.SteelIngot

		        );

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.G3Mag),
		        " F",
		        " A",
		        "FF",
		        'A', CommonProxy.SteelIngot,
		        'F', CommonProxy.MiniSteelPlate

		        );

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.PP19Mag),
                "AAF",
                "   ",
                "AAF",
                    'A', CommonProxy.SteelIngot,
                    'F', CommonProxy.MiniSteelPlate

            );

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.Glock32Mag),
                " A",
                " A",
                "AF",
                    'A', CommonProxy.SteelIngot,
                    'F', CommonProxy.MiniSteelPlate

            );

		compatibility.addShapedOreRecipe(new ItemStack(CommonProxy.HecateIIMag),
                " FF",
                "A A",
                    'A', CommonProxy.SteelIngot,
                    'F', CommonProxy.MiniSteelPlate

            );

	}
}

package vectorwing.farmersdelight.integration.jei;

import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModFileInfo;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.objectweb.asm.Type;
import vectorwing.farmersdelight.FarmersDelight;

import java.lang.annotation.ElementType;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class JEIPluginBootstrap {
	private static final String JEI_PLUGIN_CLASS_NAME = "vectorwing.farmersdelight.integration.jei.JEIPlugin";
	private static final Type JEI_PLUGIN_ANNOTATION = Type.getObjectType("mezz/jei/api/JeiPlugin");
	private static final Type JEI_PLUGIN_CLASS = Type.getObjectType(JEI_PLUGIN_CLASS_NAME.replace('.', '/'));

	private JEIPluginBootstrap() {
	}

	public static void ensurePluginDiscovery() {
		ModList modList = ModList.get();

		for (ModFileScanData scanData : modList.getAllScanData()) {
			for (ModFileScanData.AnnotationData annotation : scanData.getAnnotations()) {
				if (Objects.equals(annotation.annotationType(), JEI_PLUGIN_ANNOTATION) &&
						Objects.equals(annotation.clazz(), JEI_PLUGIN_CLASS)) {
					if (JEI_PLUGIN_CLASS_NAME.equals(annotation.memberName())) {
						FarmersDelight.LOGGER.info("Found valid JEI plugin scan data for {}", JEI_PLUGIN_CLASS_NAME);
						return;
					}

					scanData.getAnnotations().add(new ModFileScanData.AnnotationData(
							JEI_PLUGIN_ANNOTATION,
							ElementType.TYPE,
							JEI_PLUGIN_CLASS,
							JEI_PLUGIN_CLASS_NAME,
							Map.of()
					));
					FarmersDelight.LOGGER.info(
							"Repaired JEI plugin scan data member name from '{}' to '{}'",
							annotation.memberName(),
							JEI_PLUGIN_CLASS_NAME
					);
					return;
				}
			}
		}

		ModFileScanData targetScanData = modList.getAllScanData().stream()
				.filter(scanData -> ownsFarmersDelight(scanData.getIModInfoData()))
				.findFirst()
				.orElse(null);

		if (targetScanData == null) {
			FarmersDelight.LOGGER.warn("Could not find scan data for {} while bootstrapping JEI integration.", FarmersDelight.MODID);
			return;
		}

		targetScanData.getAnnotations().add(new ModFileScanData.AnnotationData(
				JEI_PLUGIN_ANNOTATION,
				ElementType.TYPE,
				JEI_PLUGIN_CLASS,
				JEI_PLUGIN_CLASS_NAME,
				Map.of()
		));
		FarmersDelight.LOGGER.info("Injected missing JEI plugin scan data for {}", JEI_PLUGIN_CLASS_NAME);
	}

	private static boolean ownsFarmersDelight(List<IModFileInfo> modFiles) {
		return modFiles.stream()
				.flatMap(modFile -> modFile.getMods().stream())
				.anyMatch(mod -> FarmersDelight.MODID.equals(mod.getModId()));
	}
}

package binnie.extrabees.alveary;

import binnie.extrabees.circuit.StimulatorCircuit;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.circuits.ChipsetManager;
import forestry.api.circuits.ICircuit;
import forestry.api.circuits.ICircuitBoard;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elec332 on 13-5-2017.
 */
public class AlvearyLogicStimulator extends AbstractAlvearyLogic {

	public AlvearyLogicStimulator() {
		this.powerUsage = 0;
		this.powered = false;
		this.modifiers = new StimulatorCircuit[0];
		this.energyStorage = new EnergyStorage(2000);
		this.inv = new ItemStackHandler(1);
	}

	private int powerUsage;
	private boolean powered;
	private StimulatorCircuit[] modifiers;
	private final IEnergyStorage energyStorage;
	private final IItemHandlerModifiable inv;

	@Override
	public void updateServer(TileEntityExtraBeesAlvearyPart tile) {
		this.modifiers = this.getCircuits();
		this.powerUsage = 0;
		for (final StimulatorCircuit beeMod : this.modifiers) {
			this.powerUsage += beeMod.getPowerUsage();
		}
		this.powered = energyStorage.extractEnergy(powerUsage, true) >= powerUsage;
	}

	@Nullable
	private ICircuitBoard getHiveFrame() {
		if (!this.inv.getStackInSlot(0).isEmpty()) {
			return ChipsetManager.circuitRegistry.getCircuitBoard(this.inv.getStackInSlot(0));
		}
		return null;
	}

	public StimulatorCircuit[] getCircuits() {
		final ICircuitBoard board = this.getHiveFrame();
		if (board == null) {
			return new StimulatorCircuit[0];
		}
		final ICircuit[] circuits = board.getCircuits();
		final List<StimulatorCircuit> mod = new ArrayList<>();
		for (final ICircuit circuit : circuits) {
			if (circuit instanceof StimulatorCircuit) {
				mod.add((StimulatorCircuit) circuit);
			}
		}
		return mod.toArray(new StimulatorCircuit[0]);
	}

	@Override
	public float getTerritoryModifier(@Nonnull final IBeeGenome genome, final float currentModifier) {
		float mod = 1.0f;
		if (!this.powered) {
			return mod;
		}
		for (final IBeeModifier beeMod : this.modifiers) {
			mod *= beeMod.getTerritoryModifier(genome, mod);
		}
		return mod;
	}

	@Override
	public float getMutationModifier(@Nonnull final IBeeGenome genome, @Nonnull final IBeeGenome mate, final float currentModifier) {
		float mod = 1.0f;
		if (!this.powered) {
			return mod;
		}
		for (final IBeeModifier beeMod : this.modifiers) {
			mod *= beeMod.getMutationModifier(genome, mate, mod);
		}
		return mod;
	}

	@Override
	public float getLifespanModifier(@Nonnull final IBeeGenome genome, @Nullable final IBeeGenome mate, final float currentModifier) {
		float mod = 1.0f;
		if (!this.powered) {
			return mod;
		}
		for (final IBeeModifier beeMod : this.modifiers) {
			mod *= beeMod.getLifespanModifier(genome, mate, mod);
		}
		return mod;
	}

	@Override
	public float getProductionModifier(@Nonnull final IBeeGenome genome, final float currentModifier) {
		float mod = 1.0f;
		if (!this.powered) {
			return mod;
		}
		for (final IBeeModifier beeMod : this.modifiers) {
			mod *= beeMod.getProductionModifier(genome, mod);
		}
		return mod;
	}

	@Override
	public float getFloweringModifier(@Nonnull final IBeeGenome genome, final float currentModifier) {
		float mod = 1.0f;
		if (!this.powered) {
			return mod;
		}
		for (final IBeeModifier beeMod : this.modifiers) {
			mod *= beeMod.getFloweringModifier(genome, mod);
		}
		return mod;
	}

	@Override
	public float getGeneticDecay(@Nonnull final IBeeGenome genome, final float currentModifier) {
		float mod = 1.0f;
		if (!this.powered) {
			return mod;
		}
		for (final IBeeModifier beeMod : this.modifiers) {
			mod *= beeMod.getGeneticDecay(genome, mod);
		}
		return mod;
	}

	@Override
	public boolean isSealed() {
		if (!this.powered) {
			return false;
		}
		for (final IBeeModifier beeMod : this.modifiers) {
			if (beeMod.isSealed()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isSelfLighted() {
		if (!this.powered) {
			return false;
		}
		for (final IBeeModifier beeMod : this.modifiers) {
			if (beeMod.isSelfLighted()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isSunlightSimulated() {
		if (!this.powered) {
			return false;
		}
		for (final IBeeModifier beeMod : this.modifiers) {
			if (beeMod.isSunlightSimulated()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isHellish() {
		if (!this.powered) {
			return false;
		}
		for (final IBeeModifier beeMod : this.modifiers) {
			if (beeMod.isHellish()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void wearOutEquipment(final int amount) {
		energyStorage.extractEnergy(powerUsage, false);
	}

	@Override
	public IEnergyStorage getEnergyStorage() {
		return energyStorage;
	}

}
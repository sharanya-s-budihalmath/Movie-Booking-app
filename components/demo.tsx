import { GlowCard } from "@/components/ui/spotlight-card";

export function Default() {
  return (
    <div className="w-screen h-screen flex flex-row items-center justify-center gap-10 custom-cursor bg-slate-950 p-8">
      <GlowCard glowColor="blue">
        <div className="flex flex-col justify-end h-full text-white">
          <h3 className="text-xl font-bold">Dune: Part Two</h3>
          <p className="text-sm text-slate-300">Sci-Fi Epic • IMAX 3D</p>
        </div>
      </GlowCard>
      <GlowCard glowColor="purple">
        <div className="flex flex-col justify-end h-full text-white">
          <h3 className="text-xl font-bold">Deadpool & Wolverine</h3>
          <p className="text-sm text-slate-300">Action Comedy • 4DX</p>
        </div>
      </GlowCard>
      <GlowCard glowColor="red">
        <div className="flex flex-col justify-end h-full text-white">
          <h3 className="text-xl font-bold">Pushpa 2: The Rule</h3>
          <p className="text-sm text-slate-300">Action Drama • Dolby Atmos</p>
        </div>
      </GlowCard>
    </div>
  );
}

export default Default;

package com.example.pokemon.vers1;

import java.util.List;

public class Pokemon {
    private String name;
    private List<TypeBox> types;
    private List<StatBox> stats;

    public String getName() {
        return name;
    }

    public List<TypeBox> getTypes() {
        return types;
    }

    public List<StatBox> getStats() {
        return stats;
    }

    // Static nested classes, da JSON sehr strukturiert ist
    // Pokemon -> Types
    public static class TypeBox {
        private int slot;
        private Type type;

        // Getter
        public Type getType() {
            return type;
        }
    }

    // Pokemon -> Types -> Type
    public static class Type {
        private String name;

        // Getter
        public String getName() {
            return name;
        }
    }

    // Pokemon -> Stats
    public static class StatBox {
        private int base_stat;
        private Stat stat;

        //Getters
        public int getBaseStat() {
            return base_stat;
        }

        public Stat getStat() {
            return stat;
        }
    }

    // Pokemon -> Stats -> Stat
    public static class Stat {
        private String name;

        // Getter
        public String getName() {
            return name;
        }
    }

}

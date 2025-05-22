package com.example.pokemon.model;

import java.util.List;

public class TypeInfo {
    private DamageRelations damage_relations;
    private List<PokemonSlot> pokemon;

    public DamageRelations getDamageRelations () {
        return damage_relations;
    }

    public List<PokemonSlot> getPokemon() {
        return pokemon;
    }


    // Type -> DamageRelations (damage_relations ->  double_damage_from / double_damage_to )
    public static class DamageRelations {
        private List<TypeRef> double_damage_to;
        private List<TypeRef> double_damage_from;

        public List<TypeRef> getDoubleDamageTo () {
            return double_damage_to;
        }

        public List<TypeRef> getDoubleDamageFrom() {
            return double_damage_from;
        }


    }
    // Type -> DamageRelations -> TypeRef   (damage_relations ->  double_damage_from / double_damage_to -> name )
    public static class TypeRef {
        private String name;

        public String getName () {
            return name;
        }
    }

    // Type -> PokemonSlot (pokemon -> pokemon)
    public static class PokemonSlot {
        private PokemonRef pokemon;
        public PokemonRef getPokemon() {
            return pokemon;
        }

        // Type -> PokemonSlot -> PokemonRef  (pokemon -> pokemon -> name)
        public static class PokemonRef {
            private String name;
            public String getName () {
                return name;
            }
        }
    }

    @Override
    public String toString() {
        return "";
    }
}

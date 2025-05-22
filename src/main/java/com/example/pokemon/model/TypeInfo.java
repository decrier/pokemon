package com.example.pokemon.model;

import java.util.List;

public class TypeInfo {
    private DamageRelations damage_relations;
    private List<PokemonSet> pokemonSet;

    public DamageRelations getDamageRelations () {
        return damage_relations;
    }

    public List<PokemonSet> getPokemonSet () {
        return pokemonSet;
    }


    // Type -> DamageRelations
    public static class DamageRelations {
        private List<TypeRef> double_damage_to;
        private List<TypeRef> double_damage_from;

        public List<TypeRef> getDoubleDamageTo () {
            return double_damage_to;
        }

        public List<TypeRef> getDouble_damage_from() {
            return double_damage_from;
        }

        public List<TypeRef> doubleDamageFrom() {
            return double_damage_from;
        }

        // Type -> DamageRelations -> TypeRef
        public static class TypeRef {
            private String name;

            public String getName () {
                return name;
            }
        }
    }

    // Type -> PokemonSet
    public static class PokemonSet {
        private PokemonRef pokemon;
        public PokemonRef getPokemon() {
            return pokemon;
        }

        // Type -> PokemonSet -> PokemonRef
        public static class PokemonRef {
            private String name;
            public String getName () {
                return name;
            }
        }
    }
}

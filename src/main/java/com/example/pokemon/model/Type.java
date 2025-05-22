package com.example.pokemon.model;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class Type {
    private DamageRelations damage_relations;

    public DamageRelations getDamageRelations () {
        return damage_relations;
    }

    // Type -> DamageRelations
    public static class DamageRelations {
        private List<TypeRef> double_damage_to;
        private List<TypeRef> double_damage_from;

        public List<TypeRef> getDoubleDamageTo () {
            return double_damage_to;
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
}

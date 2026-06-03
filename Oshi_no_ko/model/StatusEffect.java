package model;

public enum StatusEffect {

    NONE,

    // Recibe daño por turno
    POISON,

    // Pierde el turno
    STUN,

    // Reduce daño recibido
    DEFEND,

    // Aumenta ataque temporalmente
    BUFF_ATTACK
}
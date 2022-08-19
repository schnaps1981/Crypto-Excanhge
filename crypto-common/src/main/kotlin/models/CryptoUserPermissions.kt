package models

enum class CryptoUserPermissions {
    CREATE_OWN, //может создать ордер

    READ_OWN, //может читать свой ордер
    READ_ANY, //может читать ордер любого владельца

    DELETE_OWN, //может удалить свой ордер
    DELETE_ANY, //может удалить ордер любого владельца

    FILTER_OWN, //фильтр может отдать только свои ордера
    FILTER_ANY, //фильтр может отдать ордера любого владельца
}

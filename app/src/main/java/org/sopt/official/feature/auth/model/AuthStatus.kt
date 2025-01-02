package org.sopt.official.feature.auth.model

// TODO: mock서버 삭제 시 authName, phone, code는 같이 없어질 예정입니다.
enum class AuthStatus(
    val authName: String,
    val phone: String,
    val type: String,
    val code: String
) {
    REGISTER(
        authName = "Mock-Success-Register",
        phone = "01012345678",
        type = "REGISTER",
        code = "123456"
    ),
    CHANGE(
        authName = "Mock-Success-Change",
        phone = "01087654321",
        type = "CHANGE",
        code = "654321"
    ),
    SEARCH(
        authName = "Mock-Success-Search",
        phone = "01013245768",
        type = "SEARCH",
        code = "132456"
    )
}

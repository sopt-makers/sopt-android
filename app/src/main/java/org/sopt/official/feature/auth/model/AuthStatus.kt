package org.sopt.official.feature.auth.model

// TODO: mock서버 삭제 시 authName, phone, token은 같이 없어질 예정입니다.
enum class AuthStatus(
    val authName: String,
    val phone: String,
    val type: String,
    val token: String
) {
    // 회원가입
    REGISTER(
        authName = "Mock-Success-Register",
        phone = "01012345678",
        type = "REGISTER",
        token = "123456"
    ),
    // 소셜계정 변경
    CHANGE(
        authName = "Mock-Success-Change",
        phone = "01087654321",
        type = "CHANGE",
        token = "654321"
    ),
    // 소셜계정 찾기
    SEARCH(
        authName = "Mock-Success-Search",
        phone = "01013245768",
        type = "SEARCH",
        token = "132456"
    )
}

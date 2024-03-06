<div align="center">


<h1>SOPT</h1> 

**국내 최대 IT 벤처창업동아리 SOPT의 공식 Android 앱**

<p align="center">
    <img src="https://img.shields.io/badge/Kotlin-1.9.22-7F52FF?style=for-the-badge&logo=Kotlin&logoColor=white"/>
    <!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
    <img src="https://img.shields.io/badge/all_contributors-12-orange.svg?style=for-the-badge"/>
<!-- ALL-CONTRIBUTORS-BADGE:END -->
</p>
<br />  

<img width="1424" alt="image" src="https://github.com/sopt-makers/sopt-android/assets/63586451/57b33448-ac1b-42d3-b8d2-067e76635bae">
</div>

<h2>Download</h2>

<a href='https://play.google.com/store/apps/details?id=org.sopt.official'><img alt='다운로드하기 Google Play' src='https://play.google.com/intl/ko/badges/static/images/badges/ko_badge_web_generic.png' width='40%'/></a>

<h2>Tech Stack</h2>

- [Android App Architecture](https://developer.android.com/topic/architecture)
- [App Modularization](https://developer.android.com/topic/modularization)
- [Dagger-Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html), [Flow](https://kotlinlang.org/docs/flow.html)
- [Jetpack Compose](https://developer.android.com/jetpack/compose) (SOPTAMP)
- [Material 3](https://m3.material.io/)
- [Gradle Version Catalog](https://docs.gradle.org/current/userguide/platforms.html) and [Custom Convention Plugins](https://docs.gradle.org/current/samples/sample_convention_plugins.html)

<h2> Activity Graph </h2>

![Alt](https://repobeats.axiom.co/api/embed/1f0eaeebfa31162ea55bffa9cb2bb32c2a87b346.svg "Repobeats analytics image")

<h2>Set up your development environment</h2>

1. 이 레포지터리를 클론해주세요
2. 깃허브 메뉴창에서 **Settings**를 눌러주세요
<img src="https://github.com/sopt-makers/sopt-android-private/assets/54518925/a81364b9-8cb5-4ede-9436-e17cb4ba1ebe" width="60%" />

3. 메뉴 하단의 **Developer settings**를 눌러주세요
<img src="https://github.com/sopt-makers/sopt-android-private/assets/54518925/ee81fc39-d6ae-486c-8fc9-4411947ff8fe" width="60%" />

4. **Personal access tokens** 메뉴에서 Tokens(classic)을 눌러주세요
<img src="https://github.com/sopt-makers/sopt-android-private/assets/54518925/9b41a270-65fd-459b-8be0-9a770c60a0bf" width="60%" />

5. **Generate new token**을 눌러주세요
6. 다음 사항에 주의해서 Personal Access Token을 만들어주세요

- Note: Access to the SOPT Android repository 혹은 아무거나 입력
- Expiration: 7 days (No Expiration은 보안상 위험하니 권장하지 않습니다)
- Select scopes: repo (이외 scope는 건드릴 필요 없음)

7. 해당 토큰을 잘 복사해두세요. 이 토큰은 한 번만 보여지며, 다시 볼 수 없습니다.
8. 안드로이드 스튜디오에서 sopt-android 프로젝트를 열어주세요
9. `scripts/fetch_script.sh` 파일을 열어주세요
10. 상단의 실행 아이콘을 눌러주세요
<img src="https://github.com/sopt-makers/sopt-android-private/assets/54518925/91983aea-867b-4cc7-ac2b-d0b11c68d6f4" width="60%" />

11. 실행 이후에 복사해둔 토큰을 입력해주세요!
12. 초기설정이 완료되었습니다. 이후 발생하는 문제는 [이슈](https://github.com/sopt-makers/sopt-android/issues)에 등록해주세요.

<h2>Contributing</h2>

- 기능 제작 이후 Lint 및 파일의 형식 규약을 지키기 위해 다음 스크립트를 실행시켜주시기 바랍니다.

```shell
./gradlew spotlessApply -PspotlessSetLicenseHeaderYearsFromGitHistory=true
```

- 본 프로젝트는 squash merge를 사용합니다. 따라서 PR 병합을 하실 때에는 머지 옵션을 `Squash and merge`를 선택해주세요.
<img src="https://github.com/sopt-makers/sopt-android/assets/54518925/6b06b8e3-c778-4a0b-b995-9d949276c5bb" width="60%" />

## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tbody>
    <tr>
      <td align="center" valign="top" width="14.28%"><a href="http://velog.io/@l2hyunwoo"><img src="https://avatars.githubusercontent.com/u/54518925?v=4?s=100" width="100px;" alt="Hyun-Woo Lee"/><br /><sub><b>Hyun-Woo Lee</b></sub></a><br /><a href="https://github.com/sopt-makers/sopt-android/commits?author=l2hyunwoo" title="Code">💻</a> <a href="#infra-l2hyunwoo" title="Infrastructure (Hosting, Build-Tools, etc)">🚇</a> <a href="#maintenance-l2hyunwoo" title="Maintenance">🚧</a> <a href="https://github.com/sopt-makers/sopt-android/commits?author=l2hyunwoo" title="Tests">⚠️</a> <a href="https://github.com/sopt-makers/sopt-android/pulls?q=is%3Apr+reviewed-by%3Al2hyunwoo" title="Reviewed Pull Requests">👀</a> <a href="#projectManagement-l2hyunwoo" title="Project Management">📆</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/sery270"><img src="https://avatars.githubusercontent.com/u/59532818?v=4?s=100" width="100px;" alt="Seran Park"/><br /><sub><b>Seran Park</b></sub></a><br /><a href="https://github.com/sopt-makers/sopt-android/commits?author=sery270" title="Code">💻</a> <a href="https://github.com/sopt-makers/sopt-android/pulls?q=is%3Apr+reviewed-by%3Asery270" title="Reviewed Pull Requests">👀</a></td>
      <td align="center" valign="top" width="14.28%"><a href="http://antilog.tistory.com/"><img src="https://avatars.githubusercontent.com/u/45380072?v=4?s=100" width="100px;" alt="Jinsu Park"/><br /><sub><b>Jinsu Park</b></sub></a><br /><a href="https://github.com/sopt-makers/sopt-android/commits?author=jinsu4755" title="Code">💻</a> <a href="#maintenance-jinsu4755" title="Maintenance">🚧</a> <a href="https://github.com/sopt-makers/sopt-android/pulls?q=is%3Apr+reviewed-by%3Ajinsu4755" title="Reviewed Pull Requests">👀</a> <a href="#projectManagement-jinsu4755" title="Project Management">📆</a> <a href="#infra-jinsu4755" title="Infrastructure (Hosting, Build-Tools, etc)">🚇</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://velog.io/@gxstxdgxs"><img src="https://avatars.githubusercontent.com/u/81508084?v=4?s=100" width="100px;" alt="ONE (ICEMAN)"/><br /><sub><b>ONE (ICEMAN)</b></sub></a><br /><a href="https://github.com/sopt-makers/sopt-android/commits?author=hansh0101" title="Code">💻</a> <a href="#maintenance-hansh0101" title="Maintenance">🚧</a> <a href="https://github.com/sopt-makers/sopt-android/pulls?q=is%3Apr+reviewed-by%3Ahansh0101" title="Reviewed Pull Requests">👀</a> <a href="#projectManagement-hansh0101" title="Project Management">📆</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/miinjung"><img src="https://avatars.githubusercontent.com/u/39264073?v=4?s=100" width="100px;" alt="miing"/><br /><sub><b>miing</b></sub></a><br /><a href="https://github.com/sopt-makers/sopt-android/commits?author=miinjung" title="Code">💻</a> <a href="#maintenance-miinjung" title="Maintenance">🚧</a> <a href="https://github.com/sopt-makers/sopt-android/pulls?q=is%3Apr+reviewed-by%3Amiinjung" title="Reviewed Pull Requests">👀</a> <a href="#projectManagement-miinjung" title="Project Management">📆</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/hjh1161514"><img src="https://avatars.githubusercontent.com/u/61824695?v=4?s=100" width="100px;" alt="Jung Hwa Jin"/><br /><sub><b>Jung Hwa Jin</b></sub></a><br /><a href="https://github.com/sopt-makers/sopt-android/commits?author=hjh1161514" title="Code">💻</a> <a href="#maintenance-hjh1161514" title="Maintenance">🚧</a> <a href="https://github.com/sopt-makers/sopt-android/pulls?q=is%3Apr+reviewed-by%3Ahjh1161514" title="Reviewed Pull Requests">👀</a> <a href="#projectManagement-hjh1161514" title="Project Management">📆</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/KwakEuiJin"><img src="https://avatars.githubusercontent.com/u/93872496?v=4?s=100" width="100px;" alt="KwakEuiJin"/><br /><sub><b>KwakEuiJin</b></sub></a><br /><a href="https://github.com/sopt-makers/sopt-android/commits?author=KwakEuiJin" title="Code">💻</a> <a href="https://github.com/sopt-makers/sopt-android/commits?author=KwakEuiJin" title="Tests">⚠️</a></td>
    </tr>
    <tr>
      <td align="center" valign="top" width="14.28%"><a href="https://www.instagram.com/lv3isn/"><img src="https://avatars.githubusercontent.com/u/108331578?v=4?s=100" width="100px;" alt="Giovanni Junseo Kim"/><br /><sub><b>Giovanni Junseo Kim</b></sub></a><br /><a href="https://github.com/sopt-makers/sopt-android/commits?author=giovannijunseokim" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/taeheeL"><img src="https://avatars.githubusercontent.com/u/98825364?v=4?s=100" width="100px;" alt="HaeTi"/><br /><sub><b>HaeTi</b></sub></a><br /><a href="https://github.com/sopt-makers/sopt-android/commits?author=taeheeL" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/kimdahyee"><img src="https://avatars.githubusercontent.com/u/63586451?v=4?s=100" width="100px;" alt="kimdahyee._."/><br /><sub><b>kimdahyee._.</b></sub></a><br /><a href="https://github.com/sopt-makers/sopt-android/commits?author=kimdahyee" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/yxnsx"><img src="https://avatars.githubusercontent.com/u/47806943?v=4?s=100" width="100px;" alt="yunso"/><br /><sub><b>yunso</b></sub></a><br /><a href="https://github.com/sopt-makers/sopt-android/commits?author=yxnsx" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/lsakee"><img src="https://avatars.githubusercontent.com/u/93514333?v=4?s=100" width="100px;" alt="lsakee"/><br /><sub><b>lsakee</b></sub></a><br /><a href="https://github.com/sopt-makers/sopt-android/commits?author=lsakee" title="Code">💻</a></td>
    </tr>
  </tbody>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!


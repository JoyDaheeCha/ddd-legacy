package kitchenpos.application;

import kitchenpos.application.testFixture.MenuGroupFixture;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("메뉴 그룹(MenuGroup) 서비스 테스트")
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupRepository);
    }

    @DisplayName("메뉴그룹을 생성시")
    @Nested
    class CreateTest {
        @DisplayName("사용자가 입력한 메뉴명으로 메뉴가 생성된다.")
        @Test
        void nameTest() {
            // given
            var menuGroup = MenuGroupFixture.newOne("양념반 후라이드반");

            // when
            var actual = menuGroupService.create(menuGroup);

            // then
            Assertions.assertThat(actual.getName()).isEqualTo("양념반 후라이드반");
        }

        @DisplayName("메뉴명은 빈 값일 수 없다.")
        @ParameterizedTest
        @NullSource
        @EmptySource
        void nameExceptionTest(String name) {
            // given
            var menuGroup = MenuGroupFixture.newOne(name);

            // when & then
            Assertions.assertThatThrownBy(() -> menuGroupService.create(menuGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("메뉴그룹을 전부 조회한다.")
    @Test
    void findAllTest() {
        // given
        var menuGroup_레드살사 = MenuGroupFixture.newOne("레드살사");
        var menuGroup_뿌링클 = MenuGroupFixture.newOne("뿌링클");
        var menuGroups = List.of(menuGroup_레드살사, menuGroup_뿌링클);
        menuGroupRepository.saveAll(menuGroups);

        // when
        var actual = menuGroupService.findAll();

        // then
        Assertions.assertThat(actual).isEqualTo(menuGroups);
    }
}
package kitchenpos.application;

import kitchenpos.application.testFixture.MenuFixture;
import kitchenpos.application.testFixture.MenuGroupFixture;
import kitchenpos.application.testFixture.ProductFixture;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.ProductRepository;
import kitchenpos.infra.PurgomalumClient;
import org.assertj.core.api.Assertions;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴(Menu) 서비스 테스트")
class MenuServiceTest {

    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PurgomalumClient purgomalumClient;

    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuRepository, menuGroupRepository, productRepository, purgomalumClient);
    }

    @DisplayName("메뉴 생성시")
    @Nested
    class CreateTest {

        @Test
        void create() {
        }

        @DisplayName("[예외] 가격은 음수이거나 null일수 없다.")
        @ParameterizedTest
        @MethodSource("priceMethodSource")
        void invalidPriceTest(Menu menu) {
            // when & then
            Assertions.assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("[예외] 존재하지 않는 상품일 경우 예외가 발생한다.")
        @Test
        void notFoundProductTest() {
            // given
            var id = UUID.randomUUID();
            menuGroupRepository.save(MenuGroupFixture.newOne("신메뉴"));

            // when
            Mockito.when(menuGroupRepository.findById(any()))
                    .thenReturn(Optional.of(MenuGroupFixture.newOne("신메뉴")));
            Mockito.when(productRepository.findAllByIdIn(any()))
                    .thenReturn(Collections.EMPTY_LIST);

            // then
            var product = ProductFixture.newOnById(id);
            var menu = MenuFixture.newOne(product);
            Assertions.assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        static Stream<Arguments> priceMethodSource() {
            return Stream.of(
                    Arguments.arguments(MenuFixture.newOne(BigDecimal.valueOf(-1000))),
                    Arguments.arguments(MenuFixture.newOne((BigDecimal) null))
            );
        }
    }

    @Test
    void changePrice() {
    }

    @Test
    void display() {
    }

    @Test
    void hide() {
    }

    @Test
    void findAll() {
    }
}

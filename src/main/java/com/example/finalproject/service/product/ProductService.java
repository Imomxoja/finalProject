package com.example.finalproject.service.product;

import com.example.finalproject.domain.dto.request.HistoryRequest;
import com.example.finalproject.domain.dto.request.OrderRequest;
import com.example.finalproject.domain.dto.request.ProductRequest;
import com.example.finalproject.domain.dto.response.*;
import com.example.finalproject.domain.entity.category.CategoryEntity;
import com.example.finalproject.domain.entity.order.OrderEntity;
import com.example.finalproject.domain.entity.order.OrderStatus;
import com.example.finalproject.domain.entity.product.ProductEntity;
import com.example.finalproject.domain.entity.user.UserEntity;
import com.example.finalproject.repository.order.OrderRepository;
import com.example.finalproject.repository.product.ProductRepository;
import com.example.finalproject.service.BaseService;
import com.example.finalproject.service.category.CategoryService;
import com.example.finalproject.service.history.HistoryService;
import com.example.finalproject.service.order.OrderService;
import com.example.finalproject.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements BaseService<BaseResponse<ProductResponse>, ProductRequest> {
    private final ProductRepository productRepository;
    private final OrderService orderService;
    private final UserService userService;
    private final HistoryService historyService;
    private final CategoryService categoryService;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public BaseResponse<ProductResponse> create(ProductRequest productRequest) {
        if (!productRequest.getName().matches("^[A-Za-z]+$")) {
            return BaseResponse.<ProductResponse>builder()
                    .message("Name is not valid")
                    .status(400)
                    .data(null)
                    .build();
        }

        ProductEntity product = modelMapper.map(productRequest, ProductEntity.class);
        if (productRepository.existsByName(product.getName())) {
            return BaseResponse.<ProductResponse>builder()
                    .message("Already exists name " + product.getName())
                    .status(404)
                    .build();
        }

        BaseResponse<CategoryResponse> byId = categoryService.getById(productRequest.getCategory());

        if (byId.getStatus() == 400) {
            return BaseResponse.<ProductResponse>builder()
                    .message("Category not found")
                    .status(400)
                    .build();
        }

        product.setCategory(modelMapper.map(byId.getData(), CategoryEntity.class));
        productRepository.save(product);
        return BaseResponse.<ProductResponse>builder()
                .message("Product saved")
                .status(200)
                .data(modelMapper.map(product, ProductResponse.class))
                .build();

    }

    @Override
    public BaseResponse<ProductResponse> update(ProductRequest productRequest, UUID id) {
        Optional<ProductEntity> product = productRepository.findById(id);

        if (product.isPresent()) {
            try {
                if (!productRequest.getName().matches("^[A-Za-z]+$")) {
                     return BaseResponse.<ProductResponse>builder()
                             .message("Product name isn't valid")
                             .status(400)
                             .build();
                }
                if (StringUtils.isNotBlank(productRequest.getName())) {
                    product.get().setName(productRequest.getName());
                }
                if (Objects.nonNull(productRequest.getPrice())) {
                    product.get().setPrice(productRequest.getPrice());
                }
                if (Objects.nonNull(productRequest.getAmount())) {
                    product.get().setAmount(productRequest.getAmount());
                }
                if (Objects.nonNull(productRequest.getCategory())) {
                    BaseResponse<CategoryResponse> category = categoryService.getById(productRequest.getCategory());
                    product.get().setCategory(modelMapper.map(category.getData(), CategoryEntity.class));
                }
                productRepository.save(product.get());
                return BaseResponse.<ProductResponse>builder()
                        .message("Product updated ")
                        .status(200)
                        .data(modelMapper.map(product.get(), ProductResponse.class))
                        .build();
            } catch (Exception e) {
                return BaseResponse.<ProductResponse>builder()
                        .message("Product not updating")
                        .status(400)
                        .build();
            }
        }
        return BaseResponse.<ProductResponse>builder()
                .message("Product not found")
                .status(400)
                .build();
    }


    @Override
    public Boolean delete(UUID id) {
        Optional<ProductEntity> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.delete(product.get());
            return true;
        }
        return false;
    }

    @Override
    public BaseResponse<ProductResponse> getById(UUID id) {
        Optional<ProductEntity> product = productRepository.findById(id);
        if (product.isPresent()) {
            return BaseResponse.<ProductResponse>builder()
                    .message("Success")
                    .status(200)
                    .data(modelMapper.map(product, ProductResponse.class))
                    .build();
        }
        return BaseResponse.<ProductResponse>builder()
                .message("product not found")
                .status(400)
                .build();
    }

    public BaseResponse<List<ProductResponse>> findAll() {
        List<ProductEntity> list = productRepository.findAll();
        return BaseResponse.<List<ProductResponse>>builder()
                .status(200)
                .message("success")
                .data(modelMapper.map(list,
                        new TypeToken<List<ProductResponse>>() {
                        }.getType()))
                .build();
    }

    public List<ProductResponse> findByPage(Optional<Integer> page, Optional<Integer> size) {
        if (page.isPresent()) {
            if (size.isPresent()) {
                return productRepository.findAll(PageRequest.of(page.get(), size.get())).getContent()
                        .stream().map((product) -> modelMapper.map(product, ProductResponse.class))
                        .collect(Collectors.toList());
            }
            return productRepository.findAll(PageRequest.of(page.get(), 10)).getContent()
                    .stream().map((product) -> modelMapper.map(product, ProductResponse.class))
                    .collect(Collectors.toList());
        }
        return size.map(integer -> productRepository.findAll(PageRequest.of(0, integer))
                        .getContent().stream().map(product -> modelMapper.map(product, ProductResponse.class)))
                .orElseGet(() -> productRepository.findAll().stream().map(product -> modelMapper.map(product, ProductResponse.class))).collect(Collectors.toList());
    }


    public List<CategoryResponse> getChildCategory(String uuid) {
        return categoryService.getChildCategories(UUID.fromString(uuid));
    }

    public List<ProductResponse> getProduct(String uuid) {
        List<ProductEntity> productEntityByCategoryId = productRepository.findProductEntityByCategory_Id(UUID.fromString(uuid));

        if (productEntityByCategoryId.isEmpty()) {
            return null;
        }
        return productEntityByCategoryId.stream()
                .map((product) -> modelMapper.map(product, ProductResponse.class))
                .collect(Collectors.toList());
    }

    public Optional<ProductEntity> getOneProduct(UUID id) {
        return productRepository.findById(id);
    }

    public BaseResponse<ProductResponse> getByName(String data) {
        Optional<ProductEntity> productEntityByName = productRepository.findProductEntityByName(data);

        if (productEntityByName.isPresent()) {
            return BaseResponse.<ProductResponse>builder()
                    .message("success")
                    .status(200)
                    .data(modelMapper.map(productEntityByName, ProductResponse.class))
                    .build();
        }
        return BaseResponse.<ProductResponse>builder()
                .message("fail")
                .status(400)
                .build();
    }

    public BaseResponse<ProductResponse> addBasket(String data, Long chatId) {
        Optional<ProductEntity> product = productRepository.findById(UUID.fromString(data.substring(1)));
        Integer amount = Integer.parseInt(data.substring(0, 1));
        if (product.isPresent()) {
            BaseResponse<UserResponse> user = userService.getByChatId(chatId);
            if (user.getStatus() == 200) {
                if (product.get().getAmount() >= amount) {
                    orderService.create(OrderRequest.builder()
                            .product(product.get())
                            .orderState(OrderStatus.IN_CART)
                            .amount(amount)
                            .user(modelMapper.map(user.getData(), UserEntity.class))
                            .build());
                    return BaseResponse.<ProductResponse>builder()
                            .message("Successfully saved")
                            .status(200)
                            .build();
                }
                return BaseResponse.<ProductResponse>builder()
                        .message("There are not enough products")
                        .status(400)
                        .build();
            }
            return BaseResponse.<ProductResponse>builder()
                    .message("User not found")
                    .status(400)
                    .build();
        }
        return BaseResponse.<ProductResponse>builder()
                .message("Product not found")
                .status(400)
                .build();
    }

    public BaseResponse<ProductResponse> updateProduct(Integer amount, UUID id) {
        BaseResponse<ProductResponse> byId = getById(id);
        if (byId.getStatus() == 200) {
            if (byId.getData().getAmount() >= amount) {
                productRepository.updateProductAmount(amount, id);
                if (byId.getData().getAmount() == 0) {
                    productRepository.deleteById(id);
                }
                return BaseResponse.<ProductResponse>builder()
                        .message("success")
                        .status(200)
                        .build();
            }
            return BaseResponse.<ProductResponse>builder()
                    .message("Product amount not enough")
                    .status(400)
                    .build();
        }

        return BaseResponse.<ProductResponse>builder()
                .message("Product not found")
                .status(400)
                .build();
    }

    public BaseResponse<OrderResponse> orderAll(Long chatId) {
        List<OrderEntity> byChatId = orderRepository.findByChatId(chatId);

        if (!byChatId.isEmpty()) {
            for (OrderEntity orderEntity : byChatId) {
                BaseResponse<OrderResponse> order = order(orderEntity.getId().toString());
                return BaseResponse.<OrderResponse>builder()
                        .status(order.getStatus())
                        .message(order.getMessage())
                        .build();
            }
        }
        return BaseResponse.<OrderResponse>builder()
                .message("You have no orders")
                .status(400)
                .build();
    }

    public BaseResponse<OrderResponse> order(String data) {
        BaseResponse<OrderResponse> orderResponse = orderService.getById(UUID.fromString(data));

        if (orderResponse.getStatus() != 400) {
            OrderResponse order = orderResponse.getData();
            if (order.getAmount() * order.getProduct().getPrice() < order.getUser().getBalance()) {
                 if (order.getAmount() < order.getProduct().getAmount()) {
                     orderRepository.updateOrderState(orderResponse.getData().getId());
                    return BaseResponse.<OrderResponse>builder()
                            .message("Your order successfully saved. Please wait admin's response")
                            .status(200)
                            .build();
                }
                return BaseResponse.<OrderResponse>builder()
                        .message("Product amount not enough")
                        .status(400)
                        .build();
            }
            return BaseResponse.<OrderResponse>builder()
                    .message("User balance not enough")
                    .status(400)
                    .build();
        }
        return BaseResponse.<OrderResponse>builder()
                .message("Order not found")
                .status(400)
                .build();
    }

    public BaseResponse<List<ProductResponse>> findByKeyword(String keyword) {
        List<ProductEntity> product = productRepository.findByKeyword(keyword);
        return BaseResponse.<List<ProductResponse>>builder()
                .status(200)
                .message("success")
                .data(modelMapper.map(product,
                        new TypeToken<List<ProductResponse>>() {
                        }.getType()))
                .build();

    }

    private void updateHistory(List<HistoryResponse> userHistories, String name, UUID id, int amount, double totalPrice) {
        for (HistoryResponse userHistory : userHistories) {
            if (userHistory.getUser().getId().equals(id) && userHistory.getProductName().equals(name)) {
                historyService.update(HistoryRequest.builder()
                        .amount(amount)
                        .totalPrice(totalPrice)
                        .build(), userHistory.getId());
                return;
            }
        }
    }

    public BaseResponse<OrderResponse> buyOrUpdate(OrderStatus status, UUID id) {
        Optional<OrderEntity> order = orderRepository.findById(id);
        if (order.isPresent()){
//            OrderEntity map = modelMapper.map(orderRequest, OrderEntity.class);
            if (Objects.equals(status.name(),"DELIVERED")){
                return buy(order);
            }
            else if (Objects.equals(status.name(), "SHIPPING")) {
                order.get().setOrderState(OrderStatus.SHIPPING);
                OrderEntity save = orderRepository.save(order.get());
                return BaseResponse.<OrderResponse>builder()
                        .message("Order status successfully changed")
                        .status(200)
                        .data(modelMapper.map(save,OrderResponse.class))
                        .build();
            }
        }
        return BaseResponse.<OrderResponse>builder()
                .status(400)
                .message("Order not found")
                .build();
    }

    public BaseResponse<OrderResponse> buy(Optional<OrderEntity> order) {
        if (order.get().getUser().getBalance() > order.get().getAmount() * order.get().getProduct().getPrice()) {
            userService.updateBalance(order.get().getUser().getId(),
                    order.get().getAmount() * order.get().getProduct().getPrice());
        }

        BaseResponse<ProductResponse> response =
                updateProduct(order.get().getAmount(), order.get().getProduct().getId());

        if (response.getStatus() == 400) {
            return BaseResponse.<OrderResponse>builder()
                    .message(response.getMessage())
                    .status(400)
                    .build();
        }
        historyService.create(
                HistoryRequest.builder()
                        .productName(order.get().getProduct().getName())
                        .price(order.get().getProduct().getPrice())
                        .totalPrice(order.get().getAmount() * order.get().getProduct().getPrice())
                        .amount(order.get().getAmount())
                        .user(order.get().getUser())
                        .build()
        );
//                    updateHistory(historyService.getUserHistories(
//                                    update.getUser().getChatId()),
//                            update.getProduct().getName(),
//                            update.getUser().getId(),
        orderRepository.deleteById(order.get().getId());
        return BaseResponse.<OrderResponse>builder()
                .message("Order delivered")
                .status(200)
                .build();

    }
}

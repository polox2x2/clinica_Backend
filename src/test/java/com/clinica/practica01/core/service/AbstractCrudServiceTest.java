package com.clinica.practica01.core.service;

import com.clinica.practica01.core.domain.BaseEntity;
import com.clinica.practica01.core.dto.BaseResponse;
import com.clinica.practica01.core.dto.PagedResponse;
import com.clinica.practica01.core.dto.SearchParams;
import com.clinica.practica01.core.exception.ResourceNotFoundException;
import com.clinica.practica01.core.mapper.BaseMapper;
import com.clinica.practica01.core.repository.BaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AbstractCrudServiceTest {

    // --- fixtures minimas ---
    static class TestEntity extends BaseEntity { }

    static class TestResponse extends BaseResponse { }

    @SuppressWarnings("unchecked")
    private final BaseRepository<TestEntity> repository = mock(BaseRepository.class);
    @SuppressWarnings("unchecked")
    private final BaseMapper<TestEntity, String, TestResponse> mapper = mock(BaseMapper.class);

    private TestService service;

    static class TestService extends AbstractCrudService<TestEntity, String, TestResponse> {
        TestService(BaseRepository<TestEntity> r, BaseMapper<TestEntity, String, TestResponse> m) {
            super(r, m);
        }
        @Override protected String resourceName() { return "Test"; }
        @Override protected List<String> searchableFields() { return List.of("name"); }
    }

    @BeforeEach
    void setUp() {
        service = new TestService(repository, mapper);
    }

    @Test
    void create_savesActiveEntity_andReturnsMappedResponse() {
        TestEntity entity = new TestEntity();
        TestResponse response = new TestResponse();
        when(mapper.toEntity("req")).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponseWithBase(entity)).thenReturn(response);

        assertThat(service.create("req")).isSameAs(response);
        assertThat(entity.isActive()).isTrue();
        verify(repository).save(entity);
    }

    @Test
    void findById_returnsResponse_whenActive() {
        UUID id = UUID.randomUUID();
        TestEntity entity = new TestEntity();
        entity.setActive(true);
        TestResponse response = new TestResponse();
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toResponseWithBase(entity)).thenReturn(response);

        assertThat(service.findById(id)).isSameAs(response);
    }

    @Test
    void findById_throws_whenMissing() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Test");
    }

    @Test
    void findById_throws_whenInactive() {
        UUID id = UUID.randomUUID();
        TestEntity entity = new TestEntity();
        entity.setActive(false);
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void update_appliesChanges_andSaves() {
        UUID id = UUID.randomUUID();
        TestEntity entity = new TestEntity();
        entity.setActive(true);
        TestResponse response = new TestResponse();
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponseWithBase(entity)).thenReturn(response);

        assertThat(service.update(id, "req")).isSameAs(response);
        verify(mapper).updateEntity(entity, "req");
        verify(repository).save(entity);
    }

    @Test
    void delete_marksInactive_andSaves() {
        UUID id = UUID.randomUUID();
        TestEntity entity = new TestEntity();
        entity.setActive(true);
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        service.delete(id);

        ArgumentCaptor<TestEntity> captor = ArgumentCaptor.forClass(TestEntity.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().isActive()).isFalse();
    }

    @Test
    @SuppressWarnings("unchecked")
    void search_mapsPage_intoPagedResponse() {
        SearchParams params = new SearchParams();
        params.setPage(1);
        params.setPageSize(10);
        params.setSearch("foo");
        TestEntity entity = new TestEntity();
        TestResponse response = new TestResponse();
        Page<TestEntity> page = new PageImpl<>(List.of(entity), Pageable.ofSize(10), 1);
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(mapper.toResponseWithBase(entity)).thenReturn(response);

        PagedResponse<TestResponse> result = service.search(params);

        assertThat(result.getItems()).containsExactly(response);
        assertThat(result.getTotalCount()).isEqualTo(1);
        assertThat(result.getPage()).isEqualTo(1);
    }
}

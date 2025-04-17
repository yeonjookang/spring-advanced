# SPRING ADVANCED
## 필수 기능
### Lv1. 코드 개선
- [x] Early Return
- [x] 불필요한 if-else 피하기
- [x] Validation
### Lv2. N+1 문제
- getTodos 메서드에서 모든 Todo를 조회할 때, 각 Todo와 연관된 엔티티를 개별적으로 가져오고 있다.
  - [x] TodoRepository 분석하기
    - getTodos 메서드에서 findAllByOrderByModifiedAtDesc 메서드를 호출하면서 여러 개의 Todos 데이터를 가져오는데, 지연로딩의 속성으로 인하여 Todo에 연관된 User를 조회할 때 쿼리가 따로 나가게 된다.
    - 이를 해결하기 위해 Fetch Join을 사용하여, Todo를 가져올 때 연관된 User도 한 번에 같이 로딩한다. 따라서, map에서 Todo에 연관된 User을 조회할 때 추가 쿼리가 발생하지 않는다.
    - Left join으로, 연관된 User가 없어도 Todo를 조회하도록 하였다.
  - [x] @EntityGraph 기반 구현으로 수정하기
    - @EntityGraph란, JPA가 제공하는 fetch 전략 설정 방법으로, JPQL을 직접 쓰지 않고 어노테이션 기반으로 선언할 수 있다.
    - join fetch는 기본적으로 inner join이지만 left join과 outer join도 지원한다.
    - 하지만, @EntityGraph는 inner join을 지원하지 않는다. 
    - 둘 다 xToMany 관계에서 페이징이 되지 않는다.
      - Spring Data JPA의 Pageable은 내부적으로 SQL의 LIMIT / OFFSET을 사용해 페이징한다.
      - 즉, row 수를 기준으로 페이징하는데, xToMany 관계에서는 연관된 필드로 인해 row가 늘어나기 때문에, 페이징이 정상적으로 작동하지 않는다.

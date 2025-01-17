## localStorage 사용해보기

```js
// 생성
localStorage.setItem("age", "20");

// 조회
localStorage.getItem("age");

// 삭제
localStorage.removeItem("age");
```

- 수정 하는법?

  - 수정하는것 없고, 빼서 수정해서 다시 넣어야함
  - localStorage 대신 sessionStorage 붙이면 세션에 저장된 이것은 휘발성임

## array/object 저장하는법

- json으로 바꾸면됨

```js
const obj = { name: "park" };

localStorage.setItem("date", JSON.stringify(obj));

const output = localStorage.getItem("data");
console.log(JSON.parse(outpur));
```

- obj 는 저장이 안되서 JSON.stringify()를 이용해서 JSON으로 바꿔서 저장
- 다시 그것을 쓰려면 그자체로는 문자열이라서 이를또 변환해줘야함 JSON.parse()이용하면 된다.

## state 저장 localStorage

- redux-persist 쓰면
  - 자동으로 저장됨
- Jotai
- Zustand 도 리덕스랑 비슷함

## ajax 성공시/ 실패시 html 보여주려면?

- 몇초마다 자동으로 ajax 요청?
- 실패시 몇초 후 요청 재시도?
- prefetch?

  - React Query 쓰면됨

- React Query 설치

```sh
npm install react-query

//index.js

import { QueryClient, QueryClientProvider } from "react-query";

const queryClient= new QueryClient();


<QueryClientProvider client={queryClient}>
<App/>
</QueryClientProvider>
```

- useQuery사용

  ```js
  let result = useQuery("작명", () =>
    axios.get("https://codingapple1.github.io/userdata.json").then((a) => {
      return a.data;
    })
  );

  console.log(result.data); //성공시 데이터 나옴
  console.log(result.isLoading); // 로딩중일때 true
  console.log(result.error); //에러 발생시 true
  ```

  - 장점1

  ```js
  {
    result.isLoading && "로딩중";
  }
  {
    result.error && "에러남";
  }
  {
    result.data && result.data.name;
  }
  ```

  - 장점2 틈만 나면 재요청함 refetch함

  ```js
  let result = useQuery(
    "작명",
    () =>
      axios.get("https://codingapple1.github.io/userdata.json").then((a) => {
        return a.data;
      }),
    { staleTime: 2000 }
  );
  ```

- 2초마다 데이터 받아옴
- 3.실패시 retry해줌
- 4.state 공유 안해도 됨
  - 요청 같은것 해도 알아서 한번만 해도
- 5.ajax 결과 캐싱

  - 이전에 했던거 유지 해줌

  ## RTK Query

  -

RTK Query는 실은 다른 용도로도 많이 쓰는데

ajax 요청후 Redux state 변경을 하고 싶다면...

원래 Redux state변경함수 안에선 ajax요청하면 안되어서 컴포넌트 안에서 해야합니다.

근데 ajax 요청하는 코드가 다양하고 많으면 컴포넌트 안의 코드가 길어지고 관리도 귀찮은데

그런걸 Slice 안에서 관리가능하게 도와줍니다.

그리고 ajax 요청하는 코드가 100만개 있으면 그걸 편리하게 관리할 수 있게 도와줍니다.

근데 코드가 약간 더러울 뿐

# 성능 향상 팁

## props 보냈는데 왜 출력 안되는 것

- 이미지 안나오는 경우
- 코드 확인 하거나... 개발자 도구 켬
  - React Developer Tools
  - 성능등 여러가지 확인 할 수 있음

## Single Page Application 특징

- 서버에 발행 할때 하나의 큰 js에 다 소스를 넣어서 사이즈가 큼
- 그래서 좀 로딩 속도가 느림
- 그것을 분해하고 싶은 경우
  - 필요 없는것 lazy 하는 법
    ```js
    import React, { lazy, createContext, useEffect, useState } from "react";
    const Detail = lazy(() => import("./router/Detail.js"));
    const Cart = lazy(() => import("./router/Cart.js"));
    ```
    - 단점은 Cart나 Detail를 들어가면 좀 시간이 걸림
    - 이때 쓰는 것 Suspense

## 로딩 기다리면서 쓸 수 있는 Suspense

```js
import React, {
  lazy,
  Suspense,
  createContext,
  useEffect,
  useState,
} from "react";

<Suspense fallback={<div>로딩중입니다.</div>}>
  <Detail shoes={shoes}></Detail>
</Suspense>;
```

- 이렇게 lazy 해놓은 것만 하던지
- 대개는 routes 전체를 감싸기도함

## 재렌더링 막는 memo, useMeomo

```js
function Child() {
  console.log('재렌더링 됨')
  return <div>자식임</div>;
}

function Cart() {
  const { user, stock, cartItem } = useSelector((state) => {
    return state;
  });

  //console.log(user);
  //console.log(stock);
  //console.log(cartItem);

  let dispatch = useDispatch();
  const [count, setCount] = useState(0);
  return (
    <div>
      <Child></Child>
      <button onClick={() => setCount(count + 1)}>+</button>
...
```

- 이렇게 적은 경우 자식 컴포넌트도 재랜더링됨

```js
import { memo, useState } from "react";

let Child = memo(function Child() {
  console.log("재렌더링 됨");
  return <div>자식임</div>;
});
```

- 이렇게 memo로 감싸면 됨

  - 원리

    - props가 변할때만 재렌더링해줌

    ```js
    <Child count={count}></Child>
    ```

    - 이렇게 적혀있다면 재렌더링 대상이 됨
    - 성능 이슈가 잇음 신규와 기존 꺼 props 비교하기 때문에 꼭 필요한곳에 하면 좋음

## useMemo

- 함수에 완전 복잡한 것을 한다면?

  - 개선을 하고 싶다면

  ```js
  import { memo, useMemo, useState } from "react";
  function 함수() {
  return "반복 10억";
  }
  function Cart() {
  let result = useMemo(() => {
    return 함수();
  });
  ```

  - 이렇게 하면 컴포넌트 렌더링시 1회만 실행함

### useEffect와 useMemo 차이

- useEffect의 경우
  - html 다보여주고 useEffect가 실행됨
- useMemo
  - 랜더링 될때 같이 실행
- 시점의 차이임

## useTransition, useDeferredValue

- automatching batch
  - 여러개 state있을때 제일 마지막 하나만 동작함
  - 예외는 ajax, setTimeout을 이용하면 17버전은 배칭 안일어났는데 18 버전이후 이전처럼 동작함
- useTransition

  - 느린 컴포넌트 개선해줌

  ```js
  let a = new Array(10000).fill(0);
  let [name, setName] = useState("");
  
  <input
    onChange={(e) => {
      setName(e.target.value);
    }}
  />;
  {
    a.map(() => {
      return <div>{name}</div>;
    });
  }
  ```

  - 이렇게 만개가 생성되는 경우 성능저하가 있다면?
  - 사실 만개있는것을 지우거나 나누기
  - 18버전 문법인
    - useTransition쓰면됨

### useTranstion

```js
import { useState, useTransition } from "react";
let [isPending, startTranstion] = useTranstion();
```

- setName(e.target.value);
  - 여기서는 이것이 성능 저하를 일으킴, 유저가 입력할때 마다 느려짐

```js
<input
  onChange={(e) => {
    startTransition(() => {
      setName(e.target.value);
    });
  }}
/>
```

- 이렇게 하면됨
- 동작 원리
  - 브라우저 동시작업 못함
  - 한번에 하나만 작업함 그래서 느림
- 하지만 startTransition을 이용하면 좀 느린것을 지연시켜서 성능향상을 높인다.

### isPending 사용법

```js
let a = new Array(10000).fill(0);
let [name, setName] = useState("");

<input
  onChange={(e) => {
    setName(e.target.value);
  }}
/>;
{
  isPending
    ? "로딩중"
    : a.map(() => {
        return <div>{name}</div>;
      });
}
```

- 동작 중일 때 로딩중 띄우게 개발 도 가능해짐

## useDeferredValue

```js
import { useState, useTransition, useDeferredValue } from "react";

let state = useDeferredValue(name);
{
  isPending
    ? "로딩중"
    : a.map(() => {
        return <div>{state}</div>;
      });
}
```

- 이렇게 써서 그 state를 느리게 처리 할 수 도 있음

## PWA 세팅하기

```js
npx create-react-app 프로젝트명 --template cra-template-pwa


```

[(공식 튜토리얼)](https://developer.chrome.com/docs/workbox/service-worker-overview/)
[(샘플)](https://googlechrome.github.io/samples/service-worker/basic/)

## service-worker.js

```js
//index.js

//serviceWorkerRegistration.unregister();

serviceWorkerRegistration.register(); //이것 처럼 변경
```

- 이렇게 해야 생성된다.

## state 변경함수 사용시 주의

- 자바스크립트의 sync/ asynx관련 상식
- 일반적으로 synchronous 하게 처리함

- asynchronous하는법

  - ajax
  - 이벤트 리스너
  - setTimeout
    - 위를 사용하면 됨

- 기존 코드를 먼저 다 실행하고 나중에 실행함

```js
console.log(1+1);
axios get console.log(1+2);
console.log(1+3);
```

- 위의 동작은 2 4 3 이나오게 되는 것

## 리액트의 setState 함수 특징

```js
function App() {
  let [name, setName] = useState("park");
}
```

- 여기서 setName() 같은 변경함수들은
  - 전부 asynchronous(비동기적)으로 처리 됨
- 이런경우 예상치 못한 결과가 생김

### 예제 버튼을 누르면 2개 기능을 순차적으로 실행 하고 싶은 경우

```js
function App() {
  let [count, setCount] = useState(0);
  let [age, setAge] = useState(20);

  return (
    <div>
      <div>안녕 {age}</div>
      <button onClick={()=>
      setCount(count+1)
      if(age<3){
      setCouont(age+1)
      }
      }>누르면 한살 먹음/</button>
    </div>
  );
}
```

- 이렇게 했을때 우리는 age가 22임을 예상한다.
- 하지만 23임 이유는 위에서 말하 async라는 특징 때문
  - state변경함수는 async하게 처리 되는 함수라서 완료되기 까지 시간이 걸림
  - 그래서 그걸 두고 다음 코드 실행
  - 즉, 1.버튼 세번째 누르면 setCount(count+1); 이걸 실행해서 count 3ehla
  - 2.하지만 count 3 되는거 오래걸림 그래서 우선 if문 실행먼저함
  - 3.이때 아직 count 2라서 if문의 setAge가 동작하는 것임

### 그렇다면 순차적으로 어떻게 하나?

1.useEffect 생성

```js
useEffect(() => {}, [count]);
```

1.1 버튼 아래와 같이 변경
<button onClick={()=>
setCount(count+1);
}}>누르면 한살 먹기</button>

2.age+1 하는것은 useEffectd에 작성

```js
useEffect(() => {
  if (count < 3) {
    setAge(age + 1);
  }
}, [count]);
```

- 이렇게 하면 안되는 것
- 처음 페이지 로드 될때 한번 실행이 되기 때문에 이렇게 해야함 3.초기조건 제외

```js
useEffect(() => {
  if (count != 0 && count < 3) {
    setAge(age + 1);
  }
}, [count]);
```

- 이렇게 해도되고, count, age
- state에 array/ object자료형에 넣어도됨
- 하나는 굳이 state 안만들고 var 변수로 해도됨
- 변경시 HTML 자동 재렌더링이 필요한 변수들은 state로 만들면됨

## 직접 만든 백엔드 서버 가져와서 써보기

### 필요 모듈

```sh
npm install http-proxy-middleware --save
npm install axios --save
```

- setProxy.js 생성
  ```js
  const { createProxyMiddleware } = require('http-proxy-middleware');
  module.exports = function(app) {
  app.use(
    '/api',
    createProxyMiddleware({
      target: 'http://localhost:8080',	# 서버 URL or localhost:설정한포트번호
      changeOrigin: true,
    })
  );
  };
  ```
- App.js

  ```js
  import React, { useEffect, useState } from "react";
  import axios from "axios";
  function About() {
    const [hello, setHello] = useState("");
    useEffect(() => {
      axios
        .get("/api/books")
        .then((response) => setHello(response.data))
        .catch((error) => console.log(error));
    }, []);

    return <div>백엔드에서 가져온 데이터입니다 : {hello}</div>;
  }
  export default About;
  ```

- 간단하게 위처럼 하면 테스트 할 수 있음

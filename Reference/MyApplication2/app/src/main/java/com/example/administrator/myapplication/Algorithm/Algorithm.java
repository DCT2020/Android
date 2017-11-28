package com.example.administrator.myapplication.Algorithm;

import java.lang.reflect.Parameter;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by Administrator on 2017-11-28.
 */

public class Algorithm {

    public interface Runnable{
        abstract void function();
    };
    public interface  RunnableWithParams{
        abstract void function(Object[] values);
    };

    public static class DoOnce{
        private static HashMap<String, Boolean> excuteableList = new HashMap<String,Boolean>();

        /// <summary>
        /// runnable을 최초 한번만 실행합니다.
        /// Reset 함수로 다시 호출하도록 할 수 있습니다.
        /// </summary>
        /// <param name="key">함수 구분을 위한 변수입니다.
        /// 어떤 동작을 하는지 간략한 키워드를 적습니다.</param>
        /// <param name="runnable">한번만 실행하도록 할 <see cref="Runnable"/> 인자입니다.</param>
        public static void Do(String key, Runnable runnable ){
            if(!excuteableList.containsKey(key)){
                excuteableList.put(key, true);
            }

            if(excuteableList.get(key)){
                runnable.function();
                excuteableList.put(key,false);
            }
        }

        /// <summary>
        /// runnable을 최초 한번만 실행합니다.
        /// 인자값을 넘겨줄 수 있습니다.
        /// Reset 함수로 다시 호출하도록 할 수 있습니다.
        /// </summary>
        /// <param name="key">함수 구분을 위한 변수입니다.
        /// 어떤 동작을 하는지 간략한 키워드를 적습니다.</param>
        /// <param name="runnable">한번만 실행하도록 할 <see cref="RunnableWithParams"/> 인자입니다.</param>
        /// <param name="values">runnable에 넘겨질 매개변수입니다.</param>
        public static  void Do(String key, RunnableWithParams runnable, Object[] values){
            if(!excuteableList.containsKey(key)){
                excuteableList.put(key, true);
            }

            if(excuteableList.get(key)){
                runnable.function(values);
                excuteableList.put(key,false);
            }
        }

        /// <summary>
        /// 해당 키값의 DoOnce 실행카운트를 초기화합니다.
        /// </summary>
        /// <param name="key">초기화 할 DoOnce의 키값입니다.</param>
        /// <param name="runnable">초기화 동작 후 실행할 <see cref="Runnable"/> 인자입니다.</param>
        /// <param name="values">runnable에 넘겨질 매개변수입니다.</param>
        public static void Reset(String key){
            if(excuteableList.containsKey(key)){
                excuteableList.put(key, true);
            }
        }

        /// <summary>
        /// 해당 키값의 DoOnce 등록을 해제합니다.
        /// </summary>
        /// <param name="key">해제 할 DoOnce의 키값입니다.</param>
        public static void Destroy(String key){
            if(excuteableList.containsKey(key)){
                excuteableList.remove(key);
            }
        }
    }
}

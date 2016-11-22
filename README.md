# RSSWidget

apk: https://yadi.sk/d/BGi5Hk1azEKZR

Версию с mvp не стал грузить, там проблемы c отрисовкой remoteviews. То что сейчас - это старый вариант с исправленной работой AlarmManger'а. Теперь приложение должно работать корректно в слип моде. 

(upd:23.11.2016 2:19) Для api>23 использовал setExactAndAllowWhileIdle(). Он дает wakelock даже в Doze mode и позволяет запустить мой intent service. Как я понял Doze mode дает wakelock только при использовании setExactAndAllowWhileIdle().

В логах удобнее всего чекать по фильтру UpdateCheckerLog - непосредственно сам момент обновления виджета при успешной загрузки rss ленты.

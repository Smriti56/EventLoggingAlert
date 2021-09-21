
import Domain.EventLog;
import Domain.Logs;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class EventLoggingMain {


    public static void main(String args[]){

        try (Reader reader = new FileReader("src/main/resources/logfile.txt")) {

            Gson gson = new Gson();
            Logs[] logs = gson.fromJson(reader, Logs[].class);
            List<Logs> logList = Arrays.asList(logs);

            List<String> idList = logList.stream().map(x-> x.getId()).toList();

            HashSet<String> hset = new HashSet<String>(idList);
            List<EventLog> eventLogs = new ArrayList<>();
            hset.forEach(id ->{

                List<Logs> logsWithSameId = logList.stream().filter(x -> id.equals(x.getId())).collect(Collectors.toList());
                Logs obj1 = logsWithSameId.get(0);
                Logs obj2 = logsWithSameId.get(1);
                long duration = obj1.getTimestamp()-obj2.getTimestamp() > 0 ? obj1.getTimestamp()-obj2.getTimestamp() :obj2.getTimestamp()-obj1.getTimestamp();

                EventLog eventLog = new EventLog();
                eventLog.setEventId(obj1.getId());
                eventLog.setEventDuration(duration);
                eventLog.setHost(obj1.getHost());
                eventLog.setType(obj1.getType());
                eventLog.setAlert(duration > 4 ? Boolean.TRUE : Boolean.FALSE);
                eventLogs.add(eventLog);
            });
            for(EventLog l : eventLogs)
            {
                System.out.println(l.getAlert());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

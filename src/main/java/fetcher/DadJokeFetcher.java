
package fetcher;

import com.google.gson.Gson;
import dto.CombinedDTO;
import dto.DadDTO;
import dto.PeopleDTO;
import dto.PlanetDTO;
import dto.SpeciesDTO;
import dto.StarshipDTO;
import dto.VehicleDTO;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import utils.HttpUtils;


public class DadJokeFetcher {
    
     private static final String DAD_URL = "https://icanhazdadjoke.com/";
    
    
    public static String responseFromExternalDadServerParrallel(ExecutorService threadPool, Gson gson) throws InterruptedException, ExecutionException, TimeoutException {

        
           Callable<DadDTO> dadTask = new Callable<DadDTO>() {
            @Override
            public DadDTO call() throws Exception {
                String dad = HttpUtils.fetchData(DAD_URL);
                DadDTO dadDTO = gson.fromJson(dad, DadDTO.class);
                return dadDTO;
            }
        };
        

        Future<DadDTO> futureDad = threadPool.submit(dadTask);

        DadDTO dadDTO = futureDad.get(2, TimeUnit.SECONDS);

        String dad = gson.toJson(dadDTO);
        
        return dad;
    }
    
    
    
    
    
    
    
    
    
}

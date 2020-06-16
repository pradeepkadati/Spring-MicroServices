package com.appsdeveloperblog.photoapp.api.users.data;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.appsdeveloperblog.photoapp.api.users.ui.model.AlbumResponseModel;

import feign.FeignException;
import feign.hystrix.FallbackFactory;

@FeignClient(name="albums-ws",fallbackFactory=AlbumsFallBackFActory.class)
public interface AlbumsServiceClient {
	
	@GetMapping("/users/{id}/albums")
	public List<AlbumResponseModel> getAlbums(@PathVariable("id") String id);
}

@Component
class AlbumsFallBackFActory implements FallbackFactory<AlbumsServiceClient>{

	@Override
	public AlbumsServiceClient create(Throwable cause) {
	
		return new AlbumsServiceClientFallBack(cause);
	}
	
}


class AlbumsServiceClientFallBack implements AlbumsServiceClient{

	Logger logger = LoggerFactory.getLogger(AlbumsServiceClientFallBack.class);
	private Throwable cause;
	
	public AlbumsServiceClientFallBack(Throwable cause) {
		this.cause = cause;
	}
	
	@Override
	public List<AlbumResponseModel> getAlbums(String id) {
		
		if (cause instanceof FeignException &&  ((FeignException)cause).status() == 404) {
			logger.error("404 error took place when getting albums for the user id"+id);
		}else {
			logger.error("exception while fetching albums");
		}
		return new ArrayList<>();
	}
	
}



/*@Component
class AlbumsFallBack implements AlbumsServiceClient{

	@Override
	public List<AlbumResponseModel> getAlbums(String id) {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}
	
}*/

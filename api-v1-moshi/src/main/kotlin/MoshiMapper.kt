import com.crypto.api.v1.models.AccCreateRequest
import com.crypto.api.v1.models.AccDeleteRequest
import com.crypto.api.v1.models.IRequest
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

private val polymorphAdapterFactory = PolymorphicJsonAdapterFactory
    .of(IRequest::class.java, "requestType")
    .withSubtype(AccCreateRequest::class.java, AccCreateRequest::class.java.name)
    .withSubtype(AccDeleteRequest::class.java, AccDeleteRequest::class.java.name)

val moshiMapper: Moshi = Moshi.Builder()
    .add(polymorphAdapterFactory)
    .addLast(KotlinJsonAdapterFactory())
    .build()
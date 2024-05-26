import com.example.quikcartadmin.models.entities.coupons.CouponsCountResponse
import com.example.quikcartadmin.models.entities.inventory.InventoryCountResponse
import com.example.quikcartadmin.models.entities.products.ProductsCountResponse
import com.example.quikcartadmin.models.entities.products.ProductsResponse
import com.example.quikcartadmin.models.remote.datasource.RemoteDataSourceImp
import com.example.quikcartadmin.models.repository.IAdminRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AdminRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSourceImp
) : IAdminRepository {
    override suspend fun getCountOfProducts(): Flow<ProductsCountResponse> {
        return remoteDataSource.getCountOfProducts()
    }

    override suspend fun getCountOfCoupons(): Flow<CouponsCountResponse> {
        return remoteDataSource.getCountOfCoupons()
    }

    override suspend fun getCountOfInventory(): Flow<InventoryCountResponse> {
        return remoteDataSource.getCountOfInventory()
    }

    override suspend fun getAllProducts(): Flow<ProductsResponse> {
        return remoteDataSource.getAllProducts()
    }
    override suspend fun deleteProduct(productId: Long?) {
        remoteDataSource.deleteProduct(productId)
    }
}
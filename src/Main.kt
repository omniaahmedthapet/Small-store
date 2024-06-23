import java.util.Stack

data class User(val username: String, val password: String)
data class Product(val id: Int, val name: String, val price: Double, val category: String)

class ShoppingCart {
    private val cart = Stack<Product>()

    fun addProduct(product: Product) {
        cart.push(product)
        println("${product.name} added to the cart.")
    }

    fun removeProduct(product: Product) {
        if (cart.remove(product)) {
            println("${product.name} removed from the cart.")
        } else {
            println("${product.name} is not in the cart.")
        }
    }

    fun showCartContents() {
        if (cart.isEmpty()) {
            println("Cart is empty.")
        } else {
            println("Cart contents:")
            cart.forEach { product -> println("${product.name}: \$${product.price}") }
        }
    }

    fun checkout() {
        if (cart.isEmpty()) {
            println("Cart is empty. Nothing to checkout.")
        } else {
            var total = 0.0
            println("Checking out the following items:")
            while (cart.isNotEmpty()) {
                val product = cart.pop()
                println("${product.name}: \$${product.price}")
                total += product.price
            }
            println("Total: \$${total}")
        }
    }
}

class UserAccountManager(private val users: MutableList<User>) {

    fun login(username: String, password: String): Boolean {
        return users.any { it.username == username && it.password == password }
    }

    fun register(username: String, password: String): Boolean {
        if (users.any { it.username == username }) {
            println("Username already exists.")
            return false
        } else {
            users.add(User(username, password))
            println("User registered successfully.")
            return true
        }
    }

    fun logout() {
        println("Logging out...")
    }
}

fun manageCart(products: List<Product>?, cart: ShoppingCart) {
    if (products == null) {
        println("No products available in this category.")
        return
    }

    while (true) {
        println("\nEnter product ID to add to cart or 0 to go back:")
        val input = readLine()?.toIntOrNull() ?: 0
        if (input == 0) break

        val product = products.find { it.id == input }
        if (product != null) {
            cart.addProduct(product)
        } else {
            println("Invalid product ID. Please try again.")
        }
    }
}

fun main() {
    val users = mutableListOf(User("admin", "admin123"))
    val products = listOf(
        Product(1, "Shampoo", 5.99, "Personal Care"),
        Product(2, "Toothpaste", 2.99, "Personal Care"),
        Product(3, "Bread", 1.99, "Food"),
        Product(4, "Milk", 0.99, "Food"),
        Product(5, "Vacuum Cleaner", 89.99, "Home"),
        Product(6, "Dish Soap", 3.49, "Home"),
        Product(7, "Laptop", 999.99, "Electronics"),
        Product(8, "Smartphone", 499.99, "Electronics")
    )
    val categorizedProducts = products.groupBy { it.category }
    val cart = ShoppingCart()
    val userAccountManager = UserAccountManager(users)
    while (true) {
        println("\n1. Register\n2. Login\n3. Exit")
        print("Choose an option: ")
        when (readLine()?.toIntOrNull()) {
            1 -> {
                print("Enter new username: ")
                val newUsername = readLine() ?: ""
                print("Enter new password: ")
                val newPassword = readLine() ?: ""
                userAccountManager.register(newUsername, newPassword)
            }
            2 -> {
                print("Enter username: ")
                val username = readLine() ?: ""
                print("Enter password: ")
                val password = readLine() ?: ""
                if (userAccountManager.login(username, password)) {
                    println("Login successful!")
                    while (true) {
                        println("\n1. View Personal Care Products\n2. View Food Products\n3. View Home Products\n4. View Electronics Products\n5. View Cart\n6. Checkout\n7. Logout")
                        print("Choose an option: ")
                        when (readLine()?.toIntOrNull()) {
                            1 -> {
                                println("Personal Care Products:")
                                categorizedProducts["Personal Care"]?.forEach { println("${it.id}. ${it.name}: \$${it.price}") }
                                manageCart(categorizedProducts["Personal Care"], cart)
                            }
                            2 -> {
                                println("Food Products:")
                                categorizedProducts["Food"]?.forEach { println("${it.id}. ${it.name}: \$${it.price}") }
                                manageCart(categorizedProducts["Food"], cart)
                            }
                            3 -> {
                                println("Home Products:")
                                categorizedProducts["Home"]?.forEach { println("${it.id}. ${it.name}: \$${it.price}") }
                                manageCart(categorizedProducts["Home"], cart)
                            }
                            4 -> {
                                println("Electronics Products:")
                                categorizedProducts["Electronics"]?.forEach { println("${it.id}. ${it.name}: \$${it.price}") }
                                manageCart(categorizedProducts["Electronics"], cart)
                            }
                            5 -> {
                                cart.showCartContents()
                            }
                            6 -> {
                                cart.checkout()
                            }
                            7 -> {
                                userAccountManager.logout()
                                break
                            }
                            else -> {
                                println("Invalid option. Please try again.")
                            }
                        }
                    }
                } else {
                    println("Login failed. Please try again.")
                }
            }
            3 -> {
                println("Exiting...")
                return
            }
            else -> {
                println("Invalid option. Please try again.")
            }
        }
    }
}

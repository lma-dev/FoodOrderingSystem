import React, { useContext, useEffect, useState } from "react";
import "./FoodDisplay.css";
import { StoreContext } from "../../context/StoreContext";
import FoodItem from "../FoodItem/FoodItem";
import axios from "axios";
import SkeletonCard from "../Skelaton/SkeletonCard";

const FoodDisplay = ({ category }) => {
  const { food_list, url } = useContext(StoreContext);

  const [imageUrls, setImageUrls] = useState({});
  const [products, setProducts] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        setIsLoading(true);

        const token = localStorage.getItem("token");
        const headers = { Authorization: `Bearer ${token}` };

        // Fetch both APIs concurrently
        const [allProductsRes, recommendedRes] = await Promise.all([
          axios.get(`${url}/food/products`, { headers }),
          axios.get(`${url}/orders/recommended-orders`, { headers }),
        ]);

        const allProducts = allProductsRes.data;
        const recommendedProducts = recommendedRes.data;

        // Create a map of recommended product IDs with their quantities
        const recommendedMap = new Map(
          recommendedProducts.map((p) => [p.productId, p.quantity])
        );

        // Update product list with "recommended" flag and quantity
        let updatedProducts = allProducts.map((product) => ({
          ...product,
          recommended: recommendedMap.has(product.id),
          quantity: recommendedMap.get(product.id) || 0,
        }));

        // Sort: Recommended products first, then by quantity
        if (recommendedProducts.length > 0) {
          updatedProducts.sort((a, b) => {
            if (a.recommended && b.recommended) return b.quantity - a.quantity; // Sort by quantity
            return b.recommended - a.recommended; // Move recommended to the top
          });
        }

        // Remove quantity field if not needed
        updatedProducts = updatedProducts.map(({ quantity, ...rest }) => rest);

        setProducts(updatedProducts);
        fetchProductsImages(updatedProducts);
      } catch (error) {
        console.error("Error fetching products:", error);
      } finally {
        setIsLoading(false);
      }
    };

    const fetchProductsImages = (products) => {
      products.forEach((product) => {
        let newUrl = url + `/food/product/${product.id}/image`;
        axios
          .get(newUrl, {
            responseType: "blob",
          })
          .then((response) => {
            const imageUrl = URL.createObjectURL(response.data);
            setImageUrls((prevState) => ({
              ...prevState,
              [product.id]: imageUrl,
            }));
          })
          .catch((error) => {
            console.error("Error fetching product image:", error);
          });
      });
    };

    fetchProducts();
  }, []);

  if (isLoading) {
    return (
      <div className="food-display" id="food-display">
        <h2>Top dishes near you</h2>
        <div className="food-display-list">
          <SkeletonCard />
          <SkeletonCard />
          <SkeletonCard />
          <SkeletonCard />
        </div>
      </div>
    );
  }

  return (
    <div className="food-display" id="food-display">
      <h2>Top dishes near you</h2>
      <div className="food-display-list">
        {products.map((product) => {
          if (category == "All" || product.category == category) {
            return (
              <FoodItem
                key={product.id}
                id={product.id}
                name={product.name}
                description={product.description}
                price={product.price}
                image={imageUrls[product.id]}
                recommended={product.recommended}
              />
            );
          }
        })}
      </div>
    </div>
  );
};

export default FoodDisplay;

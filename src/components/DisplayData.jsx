import React from 'react'

const DisplayData = () => {
        const {
          data: projects,
          isLoading,
          isError,
        } = useQuery({
          queryKey: ["projects"],
          queryFn: async () => {
            try {
              const res = await fetch("http://localhost:8080");
              if (!res.ok) {
                throw new Error("Failed to fetch projects");
              }
              return await res.json();
            } catch (e) {
              console.error("Something went wrong:", e);
              throw e; 
            }
          }
        });
      
        if (isLoading) {
          return <h1>Loading...</h1>;
        }
        if (isError) {
          return <h1>Something went wrong</h1>;
        }
      
        return (
          <>
            {projects.map((project) => (
              <div className="col-span-12">
                
              </div>
            ))}
          </>
        );
      };

export default DisplayData

<script>
    export let message;
</script>

<p> Hello {message || "World"}!</p>

<style>
  p {
    color: #ff4000;
  }
</style>